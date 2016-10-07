
package org.drip.sequence.functional;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2016 Lakshmi Krishnamurthy
 * Copyright (C) 2015 Lakshmi Krishnamurthy
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
 * FlatMultivariateRandom contains the Implementation of the Flat Objective Function dependent on
 *  Multivariate Random Variables.
 *
 * @author Lakshmi Krishnamurthy
 */

public class FlatMultivariateRandom extends org.drip.sequence.functional.MultivariateRandom {
	private double _dblFlatValue = java.lang.Double.NaN;

	/**
	 * FlatMultivariateRandom Constructor
	 * 
	 * @param dblFlatValue The Flat Value
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public FlatMultivariateRandom (
		final double dblFlatValue)
		throws java.lang.Exception
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (_dblFlatValue = dblFlatValue))
			throw new java.lang.Exception ("FlatMultivariateRandom ctr: Invalid Inputs");
	}

	/**
	 * Retrieve the Flat Value
	 * 
	 * @return The Flat Value
	 */

	public double flatValue()
	{
		return _dblFlatValue;
	}

	@Override public int dimension()
	{
		return org.drip.function.definition.RdToR1.DIMENSION_NOT_FIXED;
	}

	@Override public double evaluate (
		final double[] adblVariate)
		throws java.lang.Exception
	{
		return _dblFlatValue;
	}
}