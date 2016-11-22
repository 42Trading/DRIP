
package org.drip.execution.generator;

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
 * OptimalTrajectoryScheme generates the Trade/Holdings List of Optimal Execution Schedule based on the
 *  Discrete/Continuous Trade Trajectory Control, the Price Walk Parameters, and the Objective Utility
 *  Function. The References are:
 * 
 * 	- Almgren, R., and N. Chriss (1999): Value under Liquidation, Risk 12 (12).
 * 
 * 	- Almgren, R., and N. Chriss (2000): Optimal Execution of Portfolio Transactions, Journal of Risk 3 (2)
 * 		5-39.
 * 
 * 	- Bertsimas, D., and A. W. Lo (1998): Optimal Control of Execution Costs, Journal of Financial Markets,
 * 		1, 1-50.
 *
 * 	- Chan, L. K. C., and J. Lakonishak (1995): The Behavior of Stock Prices around Institutional Trades,
 * 		Journal of Finance, 50, 1147-1174.
 *
 * 	- Keim, D. B., and A. Madhavan (1997): Transaction Costs and Investment Style: An Inter-exchange
 * 		Analysis of Institutional Equity Trades, Journal of Financial Economics, 46, 265-292.
 * 
 * @author Lakshmi Krishnamurthy
 */

public abstract class OptimalTrajectoryScheme {
	private org.drip.execution.risk.ObjectiveUtility _ou = null;
	private org.drip.execution.dynamics.ArithmeticPriceEvolutionParameters _apep = null;

	protected OptimalTrajectoryScheme (
		final org.drip.execution.dynamics.ArithmeticPriceEvolutionParameters apep,
		final org.drip.execution.risk.ObjectiveUtility ou)
		throws java.lang.Exception
	{
		if (null == (_apep = apep) || null == (_ou = ou))
			throw new java.lang.Exception ("OptimalTrajectoryScheme Constructor => Invalid Inputs");
	}

	/**
	 * Retrieve the Optimizer Objective Utility Function
	 * 
	 * @return The Optimizer Objective Utility Function
	 */

	public org.drip.execution.risk.ObjectiveUtility objectiveUtility()
	{
		return _ou;
	}

	/**
	 * Retrieve the Asset Arithmetic Price Walk Evolution Parameters
	 * 
	 * @return The Asset Arithmetic Price Walk Evolution Parameters
	 */

	public org.drip.execution.dynamics.ArithmeticPriceEvolutionParameters priceWalkParameters()
	{
		return _apep;
	}

	/**
	 * Invoke the Optimizer, and generate/return the Optimal Trading Trajectory Instance
	 * 
	 * @return The Optimal Trading Trajectory Instance
	 */

	abstract public org.drip.execution.optimum.EfficientTradingTrajectory generate();
}
