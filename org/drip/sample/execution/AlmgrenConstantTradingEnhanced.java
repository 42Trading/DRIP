
package org.drip.sample.execution;

import org.drip.execution.capture.TrajectoryShortfallEstimator;
import org.drip.execution.dynamics.TradingEnhancedVolatilityParameters;
import org.drip.execution.generator.*;
import org.drip.execution.impact.ParticipationRateLinear;
import org.drip.execution.optimum.*;
import org.drip.execution.risk.MeanVarianceObjectiveUtility;
import org.drip.execution.strategy.*;
import org.drip.function.definition.R1ToR1;
import org.drip.measure.gaussian.R1UnivariateNormal;
import org.drip.quant.common.FormatUtil;
import org.drip.service.env.EnvManager;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2016 Lakshmi Krishnamurthy
 * 
 *  This file is part of DRIP, a free-software/open-source library for fixed income analysts and developers -
 * 		http://www.credit-trader.org/Begin.html
 * 
 *  DRIP is a free, full featured, fixed income rates, credit, and FX analytics library with a focus towards
 *  	pricing/valuation, risk, and market making.
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
 * AlmgrenConstantTradingEnhanced demonstrates the Generation of the Optimal Trading Trajectory under the
 * 	Condition of Constant Trading Enhanced Volatility using a Numerical Optimization Technique. The
 * 	References are:
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

public class AlmgrenConstantTradingEnhanced {

	public static final void main (
		final String[] astrArgs)
		throws Exception
	{
		EnvManager.InitEnv ("");

		double dblEta = 5.e-06;
		double dblAlpha = 1.;
		double dblSigma = 1.;
		double dblLambda = 1.e-05;

		double dblX = 100000.;
		double dblT = 5.;
		int iNumInterval = 500;

		DiscreteTradingTrajectoryControl dttc = DiscreteTradingTrajectoryControl.FixedInterval (
			new OrderSpecification (
				dblX,
				dblT
			),
			iNumInterval
		);

		TradingEnhancedVolatilityParameters tevp = new TradingEnhancedVolatilityParameters (
			dblSigma,
			ParticipationRateLinear.SlopeOnly (
				dblEta
			),
			new ParticipationRateLinear (
				dblAlpha,
				0.
			)
		);

		EfficientDiscreteTradingTrajectory edtt = (EfficientDiscreteTradingTrajectory) new OptimalDiscreteTrajectoryScheme (
			dttc,
			tevp,
			new MeanVarianceObjectiveUtility (dblLambda)
		).generate();

		double[] adblExecutionTimeNode = edtt.executionTimeNode();

		double[] adblTradeList = edtt.tradeList();

		double[] adblHoldings = edtt.holdings();

		ConstantTradingEnhancedScheme ctes = ConstantTradingEnhancedScheme.Standard (
			dblX,
			dblT,
			tevp,
			dblLambda
		);

		EfficientContinuousTradingTrajectory ectt = (EfficientContinuousTradingTrajectory) ctes.generate();

		R1ToR1 r1ToR1Holdings = ectt.holdings();

		double[] adblHoldingsCF = new double[adblExecutionTimeNode.length];

		for (int i = 0; i < adblExecutionTimeNode.length; ++i)
			adblHoldingsCF[i] = r1ToR1Holdings.evaluate (adblExecutionTimeNode[i]);

		TrajectoryShortfallEstimator tse = new TrajectoryShortfallEstimator (edtt);

		R1UnivariateNormal r1un = tse.totalCostDistributionSynopsis (tevp);

		System.out.println ("\n\t|------------------------------------------------||");

		System.out.println ("\t| NUMERICAL - CLOSED FORM CONTINUOUS TRAJECTORY  ||");

		System.out.println ("\t|------------------------------------------------||");

		System.out.println ("\t|    L -> R:                                     ||");

		System.out.println ("\t|          - Execution Time Node                 ||");

		System.out.println ("\t|          - Holdings (Numerical)                ||");

		System.out.println ("\t|          - Holdings (Continuous Closed Form)   ||");

		System.out.println ("\t|          - Trade List (Numerical)              ||");

		System.out.println ("\t|          - Trade List (Continuous Closed Form) ||");

		System.out.println ("\t|------------------------------------------------||");

		for (int i = 1; i < adblExecutionTimeNode.length; ++i) {
			System.out.println ("\t| " +
				FormatUtil.FormatDouble (adblExecutionTimeNode[i], 1, 2, 1.) + " => " +
				FormatUtil.FormatDouble (adblHoldings[i] / dblX, 2, 2, 100.) + "% | " +
				FormatUtil.FormatDouble (adblHoldingsCF[i] / dblX, 2, 2, 100.) + "% | " +
				FormatUtil.FormatDouble (adblTradeList[i - 1] / dblX, 2, 2, 100.) + "% | " +
				FormatUtil.FormatDouble ((adblHoldingsCF[i] - adblHoldingsCF[i - 1]) / dblX, 2, 2, 100.) + "% ||"
			);
		}

		System.out.println ("\t|------------------------------------------------||");

		System.out.println ("\n\t|--------------------------------------------------------------------------||");

		System.out.println ("\t|  TRANSACTION COST RECONCILIATION: EXPLICIT vs. NUMERICAL vs. CLOSED FORM ||");

		System.out.println ("\t|--------------------------------------------------------------------------||");

		System.out.println (
			"\t| Transaction Cost Expectation         : " +
			FormatUtil.FormatDouble (r1un.mean(), 6, 1, 1.) + " | " +
			FormatUtil.FormatDouble (edtt.transactionCostExpectation(), 6, 1, 1.) + " | " +
			FormatUtil.FormatDouble (ectt.transactionCostExpectation(), 6, 1, 1.) + " ||"
		);

		System.out.println (
			"\t| Transaction Cost Variance (X 10^-06) : " +
			FormatUtil.FormatDouble (r1un.variance(), 6, 1, 1.e-06) + " | " +
			FormatUtil.FormatDouble (edtt.transactionCostVariance(), 6, 1, 1.e-06) + " | " +
			FormatUtil.FormatDouble (ectt.transactionCostVariance(), 6, 1, 1.e-06) + " ||"
		);

		System.out.println ("\t|--------------------------------------------------------------------------||");
	}
}
