
package org.drip.execution.generator;

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
 * Almgren2003LinearTradingEnhanced contains the Volatility Trading Trajectory generated by the Almgren
 *  (2003) Scheme under the Criterion of No-Drift AND Linear Temporary Impact Volatility. The References are:
 * 
 * 	- Almgren, R., and N. Chriss (1999): Value under Liquidation, Risk 12 (12).
 * 
 * 	- Almgren, R., and N. Chriss (2000): Optimal Execution of Portfolio Transactions, Journal of Risk 3 (2)
 * 		5-39.
 * 
 * 	- Almgren, R. (2003): Optimal Execution with Nonlinear Impact Functions and Trading-Enhanced Risk,
 * 		Applied Mathematical Finance 10 (1) 1-18.
 * 
 * 	- Almgren, R., and N. Chriss (2003): Bidding Principles, Risk 97-102.
 * 
 * 	- Bertsimas, D., and A. W. Lo (1998): Optimal Control of Execution Costs, Journal of Financial Markets,
 * 		1, 1-50.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class Almgren2003LinearTradingEnhanced extends
	org.drip.execution.generator.OptimalTrajectorySchemeDiscrete {

	/**
	 * Create the Standard Almgren2003LinearTradingEnhanced Instance
	 * 
	 * @param dblStartHoldings Trajectory Start Holdings
	 * @param dblFinishTime Trajectory Finish Time
	 * @param iNumInterval The Number of Fixed Intervals
	 * @param tevp Almgren 2003 Impact Price Walk Parameters
	 * @param dblRiskAversion The Risk Aversion Parameter
	 * 
	 * @return The Almgren2003LinearTradingEnhanced Instance
	 */

	public static final Almgren2003LinearTradingEnhanced Standard (
		final double dblStartHoldings,
		final double dblFinishTime,
		final int iNumInterval,
		final org.drip.execution.dynamics.TradingEnhancedVolatilityParameters tevp,
		final double dblRiskAversion)
	{
		try {
			return new Almgren2003LinearTradingEnhanced
				(org.drip.execution.strategy.DiscreteTradingTrajectoryControl.FixedInterval (new
					org.drip.execution.strategy.OrderSpecification (dblStartHoldings, dblFinishTime),
						iNumInterval), tevp, new org.drip.execution.risk.MeanVarianceObjectiveUtility
							(dblRiskAversion));
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private Almgren2003LinearTradingEnhanced (
		final org.drip.execution.strategy.DiscreteTradingTrajectoryControl dttc,
		final org.drip.execution.dynamics.TradingEnhancedVolatilityParameters tevp,
		final org.drip.execution.risk.MeanVarianceObjectiveUtility mvou)
		throws java.lang.Exception
	{
		super (dttc, tevp, mvou);
	}

	@Override public org.drip.execution.optimum.EfficientTradingTrajectory generate()
	{
		org.drip.execution.dynamics.TradingEnhancedVolatilityParameters tevp =
			(org.drip.execution.dynamics.TradingEnhancedVolatilityParameters) priceWalkParameters();

		double dblLambda = ((org.drip.execution.risk.MeanVarianceObjectiveUtility)
			objectiveUtility()).riskAversion();

		double dblSigma = java.lang.Double.NaN;

		try {
			dblSigma = tevp.arithmeticPriceDynamicsSettings().epochVolatility();
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		double dblTStar = java.lang.Math.sqrt (tevp.linearTemporaryExpectation().slope() / (dblLambda *
			dblSigma * dblSigma));

		return org.drip.execution.optimum.Almgren2003TradingEnhancedDiscrete.Standard
			((org.drip.execution.strategy.DiscreteTradingTrajectory) super.generate(), tevp, dblTStar,
				dblSigma * dblTStar * dblTStar / ((org.drip.execution.impact.TransactionFunctionLinear)
					(tevp.linearTemporaryVolatility().epochImpactFunction())).slope() * java.lang.Math.sqrt
						(3.));
	}
}
