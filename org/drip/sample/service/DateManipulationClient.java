
package org.drip.sample.service;

import org.drip.analytics.date.*;
import org.drip.json.parser.Converter;
import org.drip.json.simple.*;
import org.drip.service.env.EnvManager;
import org.drip.service.json.KeyHoleSkeleton;

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
 * DateManipulationClient demonstrates the Invocation and Examination of the JSON-based Date Manipulation
 *  Service Client.
 *
 * @author Lakshmi Krishnamurthy
 */

public class DateManipulationClient {

	@SuppressWarnings ("unchecked") static String IsHoliday (
		final JulianDate dt,
		final String strCalendar)
	{
		JSONObject jsonParameters = new JSONObject();

		jsonParameters.put ("Date", dt.toString());

		jsonParameters.put ("Calendar", strCalendar);

		JSONObject jsonRequest = new JSONObject();

		jsonRequest.put ("API", "DATE::ISHOLIDAY");

		jsonRequest.put ("Parameters", jsonParameters);

		return KeyHoleSkeleton.Thunker (jsonRequest.toJSONString());
	}

	@SuppressWarnings ("unchecked") static String Adjust (
		final JulianDate dt,
		final String strCalendar,
		final int iNumDaysToAdjust)
	{
		JSONObject jsonParameters = new JSONObject();

		jsonParameters.put ("Date", dt.toString());

		jsonParameters.put ("Calendar", strCalendar);

		jsonParameters.put ("DaysToAdjust", iNumDaysToAdjust);

		JSONObject jsonRequest = new JSONObject();

		jsonRequest.put ("API", "DATE::ADJUSTBUSINESSDAYS");

		jsonRequest.put ("Parameters", jsonParameters);

		return KeyHoleSkeleton.Thunker (jsonRequest.toJSONString());
	}

	@SuppressWarnings ("unchecked") static String Add (
		final JulianDate dt,
		final int iNumDaysToAdd)
	{
		JSONObject jsonParameters = new JSONObject();

		jsonParameters.put ("Date", dt.toString());

		jsonParameters.put ("DaysToAdd", iNumDaysToAdd);

		JSONObject jsonRequest = new JSONObject();

		jsonRequest.put ("API", "DATE::ADDDAYS");

		jsonRequest.put ("Parameters", jsonParameters);

		return KeyHoleSkeleton.Thunker (jsonRequest.toJSONString());
	}

	public static final void main (
		final String[] astrArgs)
		throws Exception
	{
		EnvManager.InitEnv ("");

		JulianDate dtSpot = DateUtil.CreateFromYMD (
			2016,
			DateUtil.MARCH,
			27
		);

		int iNumDays = 10;
		String strCalendar = "MXN";

		System.out.println ("\n\t|-----------------------------------------|");

		for (int i = 0; i < iNumDays; ++i) {
			JSONObject jsonResponse = (JSONObject) JSONValue.parse (
				Adjust (
					dtSpot,
					strCalendar,
					i
				)
			);

			System.out.println (
				"\t| Adjusted[" + dtSpot + " + " + i + "] = " +
				Converter.DateEntry (
					jsonResponse,
					"DateOut"
				) + " |"
			);
		}

		System.out.println ("\t|-----------------------------------------|");

		System.out.println ("\n\n\t|-------------------------------------------|");

		for (int i = 0; i < iNumDays; ++i) {
			JSONObject jsonResponse = (JSONObject) JSONValue.parse (
				Add (
					dtSpot,
					i
				)
			);

			System.out.println (
				"\t| Unadjusted[" + dtSpot + " + " + i + "] = " +
				Converter.DateEntry (
					jsonResponse,
					"DateOut"
				) + " |"
			);
		}

		System.out.println ("\t|-------------------------------------------|");

		System.out.println ("\n\n\t|---------------------------------|");

		for (int i = 0; i < iNumDays; ++i) {
			JulianDate dt = dtSpot.addDays (i);

			JSONObject jsonResponse = (JSONObject) JSONValue.parse (
				IsHoliday (
					dt,
					strCalendar
				)
			);

			System.out.println (
				"\t| Is " + dt + " a Holiday? " +
				Converter.BooleanEntry (
					jsonResponse,
					"IsHoliday"
				) + " |"
			);
		}

		System.out.println ("\t|---------------------------------|");
	}
}