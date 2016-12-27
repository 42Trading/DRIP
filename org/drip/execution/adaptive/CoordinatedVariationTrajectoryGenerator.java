
package org.drip.execution.adaptive;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2017 Lakshmi Krishnamurthy
 * Copyright (C) 2016 Lakshmi Krishnamurthy
 * 
 *  This file is part of DRIP, a free-software/open-source library for buy/side financial/trading model
 *  	libraries targeting analysts and developers
 *  	https://lakshmidrip.github.io/DRIP/
 *  
 *  DRIP is composed of four main libraries:
 *  
 *  - DRIP Fixed Income - https://lakshmidrip.github.io/DRIP-Fixed-Income/
 *  - DRIP Asset Allocation - https://lakshmidrip.github.io/DRIP-Asset-Allocation/
 *  - DRIP Numerical Optimizer - https://lakshmidrip.github.io/DRIP-Numerical-Optimizer/
 *  - DRIP Statistical Learning - https://lakshmidrip.github.io/DRIP-Statistical-Learning/
 * 
 *  - DRIP Fixed Income: Library for Instrument/Trading Conventions, Treasury Futures/Options,
 *  	Funding/Forward/Overnight Curves, Multi-Curve Construction/Valuation, Collateral Valuation and XVA
 *  	Metric Generation, Calibration and Hedge Attributions, Statistical Curve Construction, Bond RV
 *  	Metrics, Stochastic Evolution and Option Pricing, Interest Rate Dynamics and Option Pricing, LMM
 *  	Extensions/Calibrations/Greeks, Algorithmic Differentiation, and Asset Backed Models and Analytics.
 * 
 *  - DRIP Asset Allocation: Library for model libraries for MPT framework, Black Litterman Strategy
 *  	Incorporator, Holdings Constraint, and Transaction Costs.
 * 
 *  - DRIP Numerical Optimizer: Library for Numerical Optimization and Spline Functionality.
 * 
 *  - DRIP Statistical Learning: Library for Statistical Evaluation and Machine Learning.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   	you may not use this file except in compliance with the License.
 *   
 *  You may obtain a copy of the License at
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  	distributed under the License is distributed on an "AS IS" BASIS,
 *  	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  
 *  See the License for the specific language governing permissions and
 *  	limitations under the License.
 */

/**
 * CoordinatedVariationTrajectoryGenerator implements the Continuous HJB-based Single Step Optimal Cost
 *  Trajectory using the Coordinated Variation Version of the Stochastic Volatility and the Transaction
 *  Function arising from the Realization of the Market State Variable as described in the "Trading Time"
 *  Model. The References are:
 * 
 * 	- Almgren, R. F., and N. Chriss (2000): Optimal Execution of Portfolio Transactions, Journal of Risk 3
 * 		(2) 5-39.
 *
 * 	- Almgren, R. F. (2009): Optimal Trading in a Dynamic Market
 * 		https://www.math.nyu.edu/financial_mathematics/content/02_financial/2009-2.pdf.
 *
 * 	- Almgren, R. F. (2012): Optimal Trading with Stochastic Liquidity and Volatility, SIAM Journal of
 * 		Financial Mathematics  3 (1) 163-181.
 * 
 * 	- Geman, H., D. B. Madan, and M. Yor (2001): Time Changes for Levy Processes, Mathematical Finance 11 (1)
 * 		79-96.
 * 
 * 	- Jones, C. M., G. Kaul, and M. L. Lipson (1994): Transactions, Volume, and Volatility, Review of
 * 		Financial Studies & (4) 631-651.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class CoordinatedVariationTrajectoryGenerator {

	/**
	 * Flag Indicating Trade Rate Initialization from Static Trajectory
	 */

	public static final int TRADE_RATE_STATIC_INITIALIZATION = 1;

	/**
	 * Flag Indicating Trade Rate Initialization to Zero Initial Value
	 */

	public static final int TRADE_RATE_ZERO_INITIALIZATION = 2;

	private org.drip.execution.strategy.OrderSpecification _os = null;
	private int _iTradeRateInitializer = TRADE_RATE_ZERO_INITIALIZATION;
	private org.drip.execution.tradingtime.CoordinatedVariation _cv = null;
	private org.drip.execution.risk.MeanVarianceObjectiveUtility _mvou = null;
	private org.drip.execution.adaptive.NonDimensionalCostEvolver _ndce = null;

	/**
	 * CoordinatedVariationTrajectoryGenerator Constructor
	 * 
	 * @param os The Order Specification
	 * @param cv The Coordinated Variation Instance
	 * @param mvou  The Mean Variance Objective Utility Function
	 * @param ndce The Non Dimensional Cost Evolver
	 * @param iTradeRateInitializer The Trade Rate Initialization Indicator
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public CoordinatedVariationTrajectoryGenerator (
		final org.drip.execution.strategy.OrderSpecification os,
		final org.drip.execution.tradingtime.CoordinatedVariation cv,
		final org.drip.execution.risk.MeanVarianceObjectiveUtility mvou,
		final org.drip.execution.adaptive.NonDimensionalCostEvolver ndce,
		final int iTradeRateInitializer)
		throws java.lang.Exception
	{
		if (null == (_os = os) || null == (_cv = cv) || null == (_mvou = mvou) || null == (_ndce = ndce) ||
			(TRADE_RATE_STATIC_INITIALIZATION != (_iTradeRateInitializer = iTradeRateInitializer) &&
				TRADE_RATE_ZERO_INITIALIZATION != _iTradeRateInitializer))
			throw new java.lang.Exception
				("CoordinatedVariationTrajectoryGenerator Constructor => Invalid Inputs");
	}

	/**
	 * Retrieve the Trade Rate Initialization Indicator
	 * 
	 * @return The Trade Rate Initialization Indicator
	 */

	public int tradeRateInitializer()
	{
		return _iTradeRateInitializer;
	}

	/**
	 * Retrieve the Order Specification
	 * 
	 * @return The Order Specification
	 */

	public org.drip.execution.strategy.OrderSpecification orderSpecification()
	{
		return _os;
	}

	/**
	 * Retrieve the Coordinated Variation Instance
	 * 
	 * @return The Coordinated Variation Instance
	 */

	public org.drip.execution.tradingtime.CoordinatedVariation coordinatedVariationConstraint()
	{
		return _cv;
	}

	/**
	 * Retrieve the Non Dimensional Cost Evolver
	 * 
	 * @return The Non Dimensional Cost Evolver
	 */

	public org.drip.execution.adaptive.NonDimensionalCostEvolver evolver()
	{
		return _ndce;
	}

	/**
	 * Retrieve the Mean Variance Objective Utility Function
	 * 
	 * @return The Mean Variance Objective Utility Function
	 */

	public org.drip.execution.risk.MeanVarianceObjectiveUtility objectiveUtility()
	{
		return _mvou;
	}

	/**
	 * Compute The Coordinated Variation Trajectory Determinant Instance
	 * 
	 * @return The Coordinated Variation Trajectory Determinant Instance
	 */

	public org.drip.execution.adaptive.CoordinatedVariationTrajectoryDeterminant trajectoryDeterminant()
	{
		double dblExecutionSize = _os.size();

		double dblReferenceLiquidity = _cv.referenceLiquidity();

		double dblRelaxationTime = _ndce.ornsteinUnlenbeckProcess().relaxationTime();

		double dblMeanMarketUrgency = _cv.referenceVolatility() * java.lang.Math.sqrt (_mvou.riskAversion() /
			dblReferenceLiquidity);

		double dblTradeRateScale = dblExecutionSize / dblRelaxationTime;

		try {
			return new org.drip.execution.adaptive.CoordinatedVariationTrajectoryDeterminant
				(dblExecutionSize, dblRelaxationTime, dblReferenceLiquidity * dblExecutionSize *
					dblExecutionSize / dblTradeRateScale, dblTradeRateScale, dblMeanMarketUrgency,
						dblMeanMarketUrgency * dblRelaxationTime);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Retrieve the Initial Non Dimensional Cost
	 * 
	 * @param dblInitialMarketState The Initial Market State
	 * @param dblTradeRateScale The Trade Rate Scale
	 * 
	 * @return The Initial Non Dimensional Cost
	 */

	public org.drip.execution.adaptive.NonDimensionalCost initializeNonDimensionalCost (
		final double dblInitialMarketState,
		final double dblTradeRateScale)
	{
		if (TRADE_RATE_ZERO_INITIALIZATION == _iTradeRateInitializer)
			return org.drip.execution.adaptive.NonDimensionalCost.Zero();

		if (!org.drip.quant.common.NumberUtil.IsValid (dblInitialMarketState) ||
			!org.drip.quant.common.NumberUtil.IsValid (dblTradeRateScale))
			return null;

		try {
			org.drip.execution.strategy.ContinuousTradingTrajectory ctt =
				(org.drip.execution.strategy.ContinuousTradingTrajectory) new
					org.drip.execution.nonadaptive.ContinuousAlmgrenChriss (_os,
						org.drip.execution.dynamics.ArithmeticPriceEvolutionParametersBuilder.ReferenceCoordinatedVariation
						(_cv), _mvou).generate();

			if (null == ctt) return null;

			double dblNonDimensionalInstantTradeRate = ctt.tradeRate().evaluate (0.) / dblTradeRateScale;

			double dblNonDimensionalCostSensitivity = java.lang.Math.exp (dblInitialMarketState) *
				dblNonDimensionalInstantTradeRate;

			return new org.drip.execution.adaptive.NonDimensionalCost (0., dblNonDimensionalCostSensitivity,
				dblNonDimensionalCostSensitivity, dblNonDimensionalInstantTradeRate);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Generate the Continuous Coordinated Variation Dynamic Trajectory
	 * 
	 * @param adblMarketState Array of Realized Market States
	 * 
	 * @return The Continuous Coordinated Variation Dynamic Trajectory
	 */

	public org.drip.execution.adaptive.CoordinatedVariationDynamic generateDynamic (
		final double[] adblMarketState)
	{
		if (null == adblMarketState || !org.drip.quant.common.NumberUtil.IsValid (adblMarketState))
			return null;

		int iNumTimeNode = adblMarketState.length;

		if (1 >= iNumTimeNode) return null;

		double dblExecutionSize = _os.size();

		double dblReferenceLiquidity = _cv.referenceLiquidity();

		double dblTimeIncrement = _os.maxExecutionTime() / (iNumTimeNode - 1);

		double dblRelaxationTime = _ndce.ornsteinUnlenbeckProcess().relaxationTime();

		double dblMeanMarketUrgency = _cv.referenceVolatility() * java.lang.Math.sqrt (_mvou.riskAversion() /
			dblReferenceLiquidity);

		org.drip.execution.adaptive.NonDimensionalCost[] aNDC = new
			org.drip.execution.adaptive.NonDimensionalCost[iNumTimeNode];
		double[] adblNonDimensionalScaledTradeRate = new double[iNumTimeNode];
		double dblTradeRateScale = dblExecutionSize / dblRelaxationTime;
		double[] adblNonDimensionalHoldings = new double[iNumTimeNode];
		adblNonDimensionalScaledTradeRate[0] = 0.;
		adblNonDimensionalHoldings[0] = 1.;

		if (null == (aNDC[0] = initializeNonDimensionalCost (adblMarketState[0], dblTradeRateScale)))
			return null;

		for (int i = 1; i < iNumTimeNode; ++i) {
			if (null == (aNDC[i] = _ndce.evolve (aNDC[i - 1], adblMarketState[i], dblMeanMarketUrgency *
				dblRelaxationTime, (iNumTimeNode - i) * dblTimeIncrement, dblTimeIncrement)))
				return null;

			adblNonDimensionalScaledTradeRate[i] = adblNonDimensionalHoldings[i - 1] *
				aNDC[i].nonDimensionalTradeRate();

			adblNonDimensionalHoldings[i] = adblNonDimensionalHoldings[i - 1] -
				adblNonDimensionalScaledTradeRate[i] * dblTimeIncrement;
		}

		try {
			return new org.drip.execution.adaptive.CoordinatedVariationDynamic (new
				org.drip.execution.adaptive.CoordinatedVariationTrajectoryDeterminant (dblExecutionSize,
					dblRelaxationTime, dblReferenceLiquidity * dblExecutionSize * dblExecutionSize /
						dblTradeRateScale, dblTradeRateScale, dblMeanMarketUrgency, dblMeanMarketUrgency *
							dblRelaxationTime), adblNonDimensionalHoldings,
								adblNonDimensionalScaledTradeRate, aNDC);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Generate a Static, Non-adaptive Trading Trajectory Instance
	 * 
	 * @return The Static, Non-adaptive Trading Trajectory Instance
	 */

	public org.drip.execution.adaptive.CoordinatedVariationStatic generateStatic()
	{
		try {
			return new org.drip.execution.adaptive.CoordinatedVariationStatic (trajectoryDeterminant(),
				(org.drip.execution.optimum.EfficientTradingTrajectoryContinuous) new
					org.drip.execution.nonadaptive.ContinuousAlmgrenChriss (_os,
						org.drip.execution.dynamics.ArithmeticPriceEvolutionParametersBuilder.ReferenceCoordinatedVariation
						(_cv), _mvou).generate());
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Generate the Continuous Coordinated Variation Rolling Horizon Trajectory
	 * 
	 * @param adblMarketState Array of Realized Market States
	 * 
	 * @return The Continuous Coordinated Variation Rolling Horizon Trajectory
	 */

	public org.drip.execution.adaptive.CoordinatedVariationDynamic generateRollingHorizon (
		final double[] adblMarketState)
	{
		return null;
	}
}