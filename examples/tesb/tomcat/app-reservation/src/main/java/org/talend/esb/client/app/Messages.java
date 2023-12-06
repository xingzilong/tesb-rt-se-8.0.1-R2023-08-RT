/*
 * #%L
 * App Reservation Basic
 * %%
 * Copyright (c) 2006-2021 Talend Inc. - www.talend.com
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.talend.esb.client.app;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME = "org.talend.esb.client.app.messages"; //$NON-NLS-1$
	public static String CarRentalClient_BookingClass;
	public static String CarRentalClient_Brand;
	public static String CarRentalClient_CarDetails;
	public static String CarRentalClient_City;
	public static String CarRentalClient_Credits;
	public static String CarRentalClient_CustomerDetails;
	public static String CarRentalClient_DayRate;
	public static String CarRentalClient_eMail;
	public static String CarRentalClient_Insurance;
	public static String CarRentalClient_Model;
	public static String CarRentalClient_Name;
	public static String CarRentalClient_Pickup;
	public static String CarRentalClient_Pos;
	public static String CarRentalClient_POS;
	public static String CarRentalClient_ReservationDetails;
	public static String CarRentalClient_ReservationID;
	public static String CarRentalClient_Return;
	public static String CarRentalClient_Status;
	public static String CarRentalClient_Thanks;
	public static String CarRentalClient_User;
	public static String CarRentalClient_WeekEndRate;
	public static String CarRentalClient_Title;
	public static String CarRentalClient_CmdFind;
	public static String CarRentalClient_CmdReserve;
	public static String CarRentalClient_CmdCancel;
	public static String CarRentalClient_CmdClose;
	public static String CarRentalClient_CmdBack;
	public static String CarRentalClient_Offering;
	public static String CarRentalClient_SelectInfo;
	public static String CarRentalClient_Help;
	static {
		// initialize resource bundle
		// NLS.initializeMessages(BUNDLE_NAME, Messages.class);
		ResourceBundle rb = ResourceBundle.getBundle(BUNDLE_NAME);

		for (Field f : Messages.class.getDeclaredFields()) {
			int mod = f.getModifiers();
			if (f.getType().equals(String.class) && Modifier.isPublic(mod)
					&& !Modifier.isFinal(mod)) {
				try {
					f.set(Messages.class, rb.getObject(f.getName()));
				} catch (Exception e) {
					System.out.println("Exception while parsing Messages: "
							+ e.getMessage());
				}
			}
		}
	}

	private Messages() {
	}
}
