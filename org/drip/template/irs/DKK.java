
package org.drip.template.irs;

import java.util.Map;

import org.drip.analytics.cashflow.CompositePeriod;
import org.drip.analytics.date.*;
import org.drip.param.market.CurveSurfaceQuoteContainer;
import org.drip.param.valuation.ValuationParams;
import org.drip.product.rates.FixFloatComponent;
import org.drip.quant.common.FormatUtil;
import org.drip.service.env.EnvManager;
import org.drip.service.template.*;

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
 * DKK contains a Templated Pricing of the OTC Fix-Float DKK IRS Instrument.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class DKK {

	public static final void main (
		final String[] args)
		throws Exception
	{
		EnvManager.InitEnv ("");

		JulianDate dtSpot = DateUtil.Today();

		String strCurrency = "DKK";
		String strMaturityTenor = "5Y";

		FixFloatComponent irs = OTCInstrumentBuilder.FixFloatStandard (
			dtSpot,
			strCurrency,
			"ALL",
			strMaturityTenor,
			"MAIN",
			0.0206
		);

		CurveSurfaceQuoteContainer csqc = new CurveSurfaceQuoteContainer();

		csqc.setFundingState (
			LatentMarketStateBuilder.SmoothFundingCurve (
				dtSpot,
				strCurrency,
				new String[] {
					"04D", "07D", "14D", "30D", "60D"
				},
				new double[] {
					0.0017, 0.0017, 0.0018, 0.0020, 0.0023
				},
				"ForwardRate",
				new double[] {
					0.0027, 0.0032, 0.0041, 0.0054, 0.0077, 0.0104, 0.0134, 0.0160
				},
				"ForwardRate",
				new String[] {
					"04Y", "05Y", "06Y", "07Y", "08Y", "09Y", "10Y", "11Y", "12Y", "15Y", "20Y", "25Y", "30Y", "40Y", "50Y"
				},
				new double[] {
					0.0166, 0.0206, 0.0241, 0.0269, 0.0292, 0.0311, 0.0326, 0.0340, 0.0351, 0.0375, 0.0393, 0.0402, 0.0407, 0.0409, 0.0409
				},
				"SwapRate"
			)
		);

		Map<String, Double> mapOutput = irs.value (
			ValuationParams.Spot (dtSpot.julian()),
			null,
			csqc,
			null
		);

		for (Map.Entry<String, Double> me : mapOutput.entrySet())
			System.out.println ("\t | " + me.getKey() + " => " + me.getValue() + " ||");

		System.out.println ("\t |------------------------------||");

		System.out.println ("\n\n\t\t|-----------------------------------------------------------------------------------------------------------------------||");

		System.out.println ("\t\t|    Floating Stream Cash Flow Details                                                                                  ||");

		System.out.println ("\t\t|    -------- ------ ---- ---- -------                                                                                  ||");

		System.out.println ("\t\t|               Start Date                                                                                              ||");

		System.out.println ("\t\t|               End Date                                                                                                ||");

		System.out.println ("\t\t|               Pay Date                                                                                                ||");

		System.out.println ("\t\t|               FX Fixing Date                                                                                          ||");

		System.out.println ("\t\t|               Base Notional                                                                                           ||");

		System.out.println ("\t\t|               Period DCF                                                                                              ||");

		System.out.println ("\t\t|               Tenor                                                                                                   ||");

		System.out.println ("\t\t|               Funding Label                                                                                           ||");

		System.out.println ("\t\t|               Forward Label                                                                                           ||");

		System.out.println ("\t\t|               Pay Discount Factor                                                                                     ||");

		System.out.println ("\t\t|               Coupon Rate                                                                                             ||");

		System.out.println ("\t\t|-----------------------------------------------------------------------------------------------------------------------||");

		for (CompositePeriod cp : irs.derivedStream().cashFlowPeriod())
			System.out.println ("\t\t| [" +
				new JulianDate (cp.startDate()) + " - " +
				new JulianDate (cp.endDate()) + "] => " +
				new JulianDate (cp.payDate()) + " | " +
				new JulianDate (cp.fxFixingDate()) + " | " +
				FormatUtil.FormatDouble (cp.baseNotional(), 1, 4, 1.) + " | " +
				FormatUtil.FormatDouble (cp.couponDCF(), 1, 4, 1.) + " | " +
				cp.tenor() + " | " +
				cp.fundingLabel().fullyQualifiedName() + " | " +
				cp.forwardLabel().fullyQualifiedName() + " | " +
				FormatUtil.FormatDouble (cp.df (csqc), 1, 4, 1.) + " | " +
				FormatUtil.FormatDouble (cp.couponMetrics (dtSpot.julian(), csqc).rate(), 1, 2, 100.) + "% ||"
			);

		System.out.println ("\t\t|-----------------------------------------------------------------------------------------------------------------------||");

		System.out.println ("\n\n\t\t|--------------------------------------------------------------------------------------------------------||");

		System.out.println ("\t\t|    Fixed Stream Cash Flow Details                                                                      ||");

		System.out.println ("\t\t|    ----- ------ ---- ---- -------                                                                      ||");

		System.out.println ("\t\t|               Start Date                                                                               ||");

		System.out.println ("\t\t|               End Date                                                                                 ||");

		System.out.println ("\t\t|               Pay Date                                                                                 ||");

		System.out.println ("\t\t|               FX Fixing Date                                                                           ||");

		System.out.println ("\t\t|               Base Notional                                                                            ||");

		System.out.println ("\t\t|               Period DCF                                                                               ||");

		System.out.println ("\t\t|               Tenor                                                                                    ||");

		System.out.println ("\t\t|               Funding Label                                                                            ||");

		System.out.println ("\t\t|               Pay Discount Factor                                                                      ||");

		System.out.println ("\t\t|               Coupon Rate                                                                              ||");

		System.out.println ("\t\t|--------------------------------------------------------------------------------------------------------||");

		for (CompositePeriod cp : irs.referenceStream().cashFlowPeriod())
			System.out.println ("\t\t| [" +
				new JulianDate (cp.startDate()) + " - " +
				new JulianDate (cp.endDate()) + "] => " +
				new JulianDate (cp.payDate()) + " | " +
				new JulianDate (cp.fxFixingDate()) + " | " +
				FormatUtil.FormatDouble (cp.baseNotional(), 1, 4, 1.) + " | " +
				FormatUtil.FormatDouble (cp.couponDCF(), 1, 4, 1.) + " | " +
				cp.tenor() + " | " +
				cp.fundingLabel().fullyQualifiedName() + " | " +
				FormatUtil.FormatDouble (cp.df (csqc), 1, 4, 1.) + " | " +
				FormatUtil.FormatDouble (cp.couponMetrics (dtSpot.julian(), csqc).rate(), 1, 2, 100.) + "% ||"
			);

		System.out.println ("\t\t|--------------------------------------------------------------------------------------------------------||");
	}
}