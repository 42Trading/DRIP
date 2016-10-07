
package org.drip.function.definition;

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
 * SizedVector holds the R^d Unit Direction Vector along with its Magnitude.
 *
 * @author Lakshmi Krishnamurthy
 */

public class SizedVector {
	private double _dblMagnitude = java.lang.Double.NaN;
	private org.drip.function.definition.UnitVector _uv = null;

	/**
	 * Construct an Instance of the Sized Vector from the Input Array
	 * 
	 * @param adbl The Input Double Array
	 * 
	 * @return The Sized Vector Instance
	 */

	public static final SizedVector Standard (
		final double[] adbl)
	{
		if (null == adbl) return null;

		double dblModulus = 0.;
		int iDimension = adbl.length;
		double[] adblComponent = 0 == iDimension ? null : new double[iDimension];

		if (0 == iDimension) return null;

		for (int i = 0; i < iDimension; ++i) {
			if (!org.drip.quant.common.NumberUtil.IsValid (adbl[i])) return null;

			dblModulus += adbl[i] * adbl[i];
		}

		if (0. == dblModulus) return null;

		dblModulus = java.lang.Math.sqrt (dblModulus);

		for (int i = 0; i < iDimension; ++i)
			adblComponent[i] = adbl[i] / dblModulus;

		try {
			return new SizedVector (new org.drip.function.definition.UnitVector (adblComponent), dblModulus);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * SizedVector Constructor
	 * 
	 * @param uv The Unit Vector
	 * @param dblMagnitude Magnitude of the Vector
	 * 
	 * @throws java.lang.Exception Thriwn if the Inputs are Invalid
	 */

	public SizedVector (
		final org.drip.function.definition.UnitVector uv,
		final double dblMagnitude)
		throws java.lang.Exception
	{
		if (null == (_uv = uv) || !org.drip.quant.common.NumberUtil.IsValid (_dblMagnitude = dblMagnitude))
			throw new java.lang.Exception ("SizedVector Constructor => Invalid Inputs");
	}

	/**
	 * Retrieve the Unit Direction Vector
	 * 
	 * @return The Unit Vector Direction Instance
	 */

	public org.drip.function.definition.UnitVector direction()
	{
		return _uv;
	}

	/**
	 * Retrieve the Vector Magnitude
	 * 
	 * @return The Vector Magnitude
	 */

	public double magnitude()
	{
		return _dblMagnitude;
	}
}