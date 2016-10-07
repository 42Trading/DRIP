
package org.drip.product.params;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2016 Lakshmi Krishnamurthy
 * Copyright (C) 2015 Lakshmi Krishnamurthy
 * Copyright (C) 2014 Lakshmi Krishnamurthy
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
 * LastTradingDateSeting contains the Last Trading Date Generation Scheme for the given Option.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class LastTradingDateSetting {

	/**
	 * Quarterly Mid-Curve Option
	 */

	public static final int MID_CURVE_OPTION_QUARTERLY = 0;

	/**
	 * Serial Mid-Curve Option
	 */

	public static final int MID_CURVE_OPTION_SERIAL = 1;

	/**
	 * Generic Mid-Curve Option
	 */

	public static final int MID_CURVE_OPTION = 2;

	private int _iMidCurveOptionType = -1;
	private java.lang.String _strLastTradeExerciseLag = "";
	private int _iLastTradingDate = java.lang.Integer.MIN_VALUE;

	/**
	 * Retrieve the String Version of the Mid Curve Option Setting
	 * 
	 * @param iMidCurveOptionType The Mid Curve Option Type
	 * 
	 * @return String Version of the Mid Curve Option Setting
	 */

	public static final java.lang.String MidCurveOptionString (
		final int iMidCurveOptionType)
	{
		if (MID_CURVE_OPTION_QUARTERLY == iMidCurveOptionType) return "QUARTERLY";

		if (MID_CURVE_OPTION_SERIAL == iMidCurveOptionType) return "SERIAL";

		if (MID_CURVE_OPTION == iMidCurveOptionType) return "REGULAR";

		return null;
	}

	/**
	 * LastTradingDateSetting Constructor
	 * 
	 * @param iMidCurveOptionType Mid Curve Option Type
	 * @param strLastTradeExerciseLag Lag between the Exercise Date and the Last Option Trading Date
	 * @param iLastTradingDate The Last Trading Date
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are invalid
	 */

	public LastTradingDateSetting (
		final int iMidCurveOptionType,
		final java.lang.String strLastTradeExerciseLag,
		final int iLastTradingDate)
		throws java.lang.Exception
	{
		if (MID_CURVE_OPTION_QUARTERLY != (_iMidCurveOptionType = iMidCurveOptionType) &&
			MID_CURVE_OPTION_SERIAL != _iMidCurveOptionType && MID_CURVE_OPTION != _iMidCurveOptionType)
			throw new java.lang.Exception ("LastTradingDateSetting ctr => Invalid Inputs");

		_iLastTradingDate = iLastTradingDate;
		_strLastTradeExerciseLag = strLastTradeExerciseLag;

		if ((MID_CURVE_OPTION == _iMidCurveOptionType && (null == _strLastTradeExerciseLag ||
			_strLastTradeExerciseLag.isEmpty())) || (MID_CURVE_OPTION_SERIAL == _iMidCurveOptionType &&
				!org.drip.quant.common.NumberUtil.IsValid (_iLastTradingDate)))
			throw new java.lang.Exception ("LastTradingDateSetting ctr => Invalid Inputs");
	}

	/**
	 * Retrieve the Mid-Curve Option Type
	 * 
	 * @return The Mid-Curve Option Type
	 */

	public int midCurveOptionType()
	{
		return _iMidCurveOptionType;
	}

	/**
	 * Retrieve the Lag between the Last Trading and Exercise Date
	 * 
	 * @return The Lag between the Last Trading and Exercise Date
	 */

	public java.lang.String lastTradeExerciseLag()
	{
		return _strLastTradeExerciseLag;
	}

	/**
	 * Retrieve the Last Trading Date
	 * 
	 * @return The Last Trading Date
	 */

	public double lastTradingDate()
	{
		return _iLastTradingDate;
	}

	/**
	 * Compute the Last Trading Date
	 * 
	 * @param iUnderlyingLastTradingDate The Last Trading Date for the Underlying
	 * @param strCalendar The Calendar
	 * 
	 * @return The Last Trading Date
	 */

	public int lastTradingDate (
		final int iUnderlyingLastTradingDate,
		final java.lang.String strCalendar)
	{
		if (MID_CURVE_OPTION_SERIAL == _iMidCurveOptionType) return _iLastTradingDate;

		if (MID_CURVE_OPTION_QUARTERLY == _iMidCurveOptionType) return iUnderlyingLastTradingDate;

		return new org.drip.analytics.date.JulianDate (iUnderlyingLastTradingDate).subtractTenorAndAdjust
			(_strLastTradeExerciseLag, strCalendar).julian();
	}

	@Override public java.lang.String toString()
	{
		java.lang.String str = "MID CURVE OPTION::" + MidCurveOptionString (_iMidCurveOptionType);

		if (MID_CURVE_OPTION_QUARTERLY == _iMidCurveOptionType) return str;

		if (MID_CURVE_OPTION == _iMidCurveOptionType) return str + "@" + _strLastTradeExerciseLag;

		if (MID_CURVE_OPTION_SERIAL == _iMidCurveOptionType) return str + "@" + _iLastTradingDate;

		return null;
	}
}