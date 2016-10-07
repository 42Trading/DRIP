
package org.drip.portfolioconstruction.bayesian;

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
 * PriorControlSpecification contains the Black Litterman Prior Specification Settings. The References are:
 *  
 *  - He. G., and R. Litterman (1999): The Intuition behind the Black-Litterman Model Portfolios, Goldman
 *  	Sachs Asset Management
 *  
 *  - Idzorek, T. (2005): A Step-by-Step Guide to the Black-Litterman Model: Incorporating User-Specified
 *  	Confidence Levels, Ibbotson Associates, Chicago
 *
 * @author Lakshmi Krishnamurthy
 */

public class PriorControlSpecification {
	private double _dblTau = java.lang.Double.NaN;
	private boolean _bAlternateReferenceModel = false;
	private double _dblRiskFreeRate = java.lang.Double.NaN;

	/**
	 * PriorControlSpecification Constructor
	 * 
	 * @param bAlternateReferenceModel TRUE => Use Alternate Reference in place of the Traditional Black
	 * 	Litterman Model
	 * @param dblRiskFreeRate The Risk Free Rate
	 * @param dblTau The Asset Space Excess Returns "Confidence" Parameter
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public PriorControlSpecification (
		final boolean bAlternateReferenceModel,
		final double dblRiskFreeRate,
		final double dblTau)
		throws java.lang.Exception
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (_dblRiskFreeRate = dblRiskFreeRate) ||
			!org.drip.quant.common.NumberUtil.IsValid (_dblTau = dblTau))
			throw new java.lang.Exception ("PriorControlSpecification Constructor => Invalid Inputs");

		_bAlternateReferenceModel = bAlternateReferenceModel;
	}

	/**
	 * Retrieve the Flag indicating if the Alternate Reference Model is to be used
	 * 
	 * @return TRUE => Use Alternate Reference in place of the Traditional Black Litterman Model
	 */

	public boolean useAlternateReferenceModel()
	{
		return _bAlternateReferenceModel;
	}

	/**
	 * Retrieve the Risk Free Rate
	 * 
	 * @return The Risk Free Rate
	 */

	public double riskFreeRate()
	{
		return _dblRiskFreeRate;
	}

	/**
	 * Retrieve Tau
	 * 
	 * @return Tau
	 */

	public double tau()
	{
		return _dblTau;
	}
}