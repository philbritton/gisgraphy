/*******************************************************************************
 *   Gisgraphy Project 
 * 
 *   This library is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU Lesser General Public
 *   License as published by the Free Software Foundation; either
 *   version 2.1 of the License, or (at your option) any later version.
 * 
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *   Lesser General Public License for more details.
 * 
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA
 * 
 *  Copyright 2008  Gisgraphy project 
 *  David Masclet <davidmasclet@gisgraphy.com>
 *  
 *  
 *******************************************************************************/
package com.gisgraphy.importer;

import com.gisgraphy.domain.geoloc.entity.ATM;
import com.gisgraphy.domain.geoloc.entity.AdmBuilding;
import com.gisgraphy.domain.geoloc.entity.Bank;
import com.gisgraphy.domain.geoloc.entity.Bar;
import com.gisgraphy.domain.geoloc.entity.Bench;
import com.gisgraphy.domain.geoloc.entity.Building;
import com.gisgraphy.domain.geoloc.entity.BusStation;
import com.gisgraphy.domain.geoloc.entity.Casino;
import com.gisgraphy.domain.geoloc.entity.Cemetery;
import com.gisgraphy.domain.geoloc.entity.CourtHouse;
import com.gisgraphy.domain.geoloc.entity.Dentist;
import com.gisgraphy.domain.geoloc.entity.Doctor;
import com.gisgraphy.domain.geoloc.entity.EmergencyPhone;
import com.gisgraphy.domain.geoloc.entity.FerryTerminal;
import com.gisgraphy.domain.geoloc.entity.FireStation;
import com.gisgraphy.domain.geoloc.entity.Fountain;
import com.gisgraphy.domain.geoloc.entity.Fuel;
import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.geoloc.entity.Hospital;
import com.gisgraphy.domain.geoloc.entity.House;
import com.gisgraphy.domain.geoloc.entity.Library;
import com.gisgraphy.domain.geoloc.entity.Museum;
import com.gisgraphy.domain.geoloc.entity.NightClub;
import com.gisgraphy.domain.geoloc.entity.Parking;
import com.gisgraphy.domain.geoloc.entity.Pharmacy;
import com.gisgraphy.domain.geoloc.entity.PolicePost;
import com.gisgraphy.domain.geoloc.entity.PostOffice;
import com.gisgraphy.domain.geoloc.entity.Prison;
import com.gisgraphy.domain.geoloc.entity.Religious;
import com.gisgraphy.domain.geoloc.entity.Rental;
import com.gisgraphy.domain.geoloc.entity.Restaurant;
import com.gisgraphy.domain.geoloc.entity.School;
import com.gisgraphy.domain.geoloc.entity.Shop;
import com.gisgraphy.domain.geoloc.entity.SwimmingPool;
import com.gisgraphy.domain.geoloc.entity.Taxi;
import com.gisgraphy.domain.geoloc.entity.Telephone;
import com.gisgraphy.domain.geoloc.entity.Theater;
import com.gisgraphy.domain.geoloc.entity.Toilet;
import com.gisgraphy.domain.geoloc.entity.VendingMachine;
import com.gisgraphy.domain.geoloc.entity.Veterinary;


/**
 * associates the amenity tag of osm to a palcetype. 
 * list of values based on http://taginfo.openstreetmap.org/keys/?key=amenity#values
 * 
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class OsmAmenityToPlacetype {
	
	public GisFeature getObject(String amenity){
		if (amenity == null){
			return new GisFeature();
		} 
		String a = amenity.trim().toLowerCase();
		GisFeature gisfeature = new GisFeature();
		if ("".equals(amenity)){
			gisfeature.setAmenity(amenity);
			return gisfeature;
		}
		//take care of case, always put in lower case
		if ("parking".equals(a)){
			gisfeature =  new Parking();
		} else if ("school".equals(a)){
			gisfeature =  new School();
		} else  if ("place_of_worship".equals(a)){
			gisfeature =  new Religious();
		} else  if ("restaurant".equals(a)){
			gisfeature =  new Restaurant();
		} else  if ("fuel".equals(a)){
			gisfeature =  new Fuel();
		} else  if ("bench".equals(a)){
			gisfeature =  new Bench();
		} else  if ("grave_yard".equals(a)){
			gisfeature =  new Cemetery();
		} else  if ("post_box".equals(a)){
			gisfeature =  new PostOffice();
		} else  if ("bank".equals(a)){
			gisfeature =  new Bank();
		} else  if ("fast_food".equals(a)){
			gisfeature =  new Restaurant();
		} else  if ("cafe".equals(a)){
			gisfeature =  new Restaurant();
		} else  if ("kindergarten".equals(a)){
			gisfeature =  new School();
		} else  if ("recycling".equals(a)){
			gisfeature =  new GisFeature();
		} else  if ("public_building".equals(a)){
			gisfeature =  new Building();
		} else  if ("hospital".equals(a)){
			gisfeature =  new Hospital();
		} else  if ("pharmacy".equals(a)){
			gisfeature =  new Pharmacy();
		} else  if ("post_office".equals(a)){
			gisfeature =  new PostOffice();
		} else  if ("pub".equals(a)){
			gisfeature =  new Bar();
		} else  if ("bicycle_parking".equals(a)){
			gisfeature =  new Parking();
		} else  if ("telephone".equals(a)){
			gisfeature =  new Telephone();
		} else  if ("toilets".equals(a)){
			gisfeature =  new Toilet();
		} else  if ("waste_basket".equals(a)){
			gisfeature =  new GisFeature();
		} else  if ("atm".equals(a)){
			gisfeature =  new ATM();
		} else  if ("drinking_water".equals(a)){
			gisfeature =  new GisFeature();
		} else  if ("fire_station".equals(a)){
			gisfeature =  new FireStation();
		} else  if ("police".equals(a)){
			gisfeature =  new PolicePost();
		} else  if ("shelter".equals(a)){
			gisfeature =  new GisFeature();
		} else  if ("bar".equals(a)){
			gisfeature =  new Bar();
		} else  if ("swimming_pool".equals(a)){
			gisfeature =  new SwimmingPool();
		} else  if ("townhall".equals(a)){
			gisfeature =  new AdmBuilding();
		} else  if ("parking_space".equals(a)){
			gisfeature =  new Parking();
		} else  if ("library".equals(a)){
			gisfeature =  new Library();
		} else  if ("fountain".equals(a)){
			gisfeature =  new Fountain();
		} else  if ("vending_machine".equals(a)){
			gisfeature =  new VendingMachine();
		} else  if ("university".equals(a)){
			gisfeature =  new School();
		} else  if ("doctors".equals(a)){
			gisfeature =  new Doctor();
		} else  if ("social_facility".equals(a)){
			gisfeature =  new AdmBuilding();
		} else  if ("bus_station".equals(a)){
			gisfeature =  new BusStation();
		} else  if ("college".equals(a)){
			gisfeature =  new School();
		} else  if ("car_wash".equals(a)){
			gisfeature =  new GisFeature();
		} else  if ("marketplace".equals(a)){
			gisfeature =  new Shop();
		} else  if ("emergency_phone".equals(a)){
			gisfeature =  new EmergencyPhone();
		} else  if ("dentist".equals(a)){
			gisfeature =  new Dentist();
		} else  if ("theatre".equals(a)){
			gisfeature =  new Theater();
		} else  if ("waste_disposal".equals(a)){
			gisfeature =  new GisFeature();
		} else  if ("taxi".equals(a)){
			gisfeature =  new Taxi();
		} else  if ("community_centre".equals(a)){
			gisfeature =  new GisFeature();
		} else  if ("cinema".equals(a)){
			gisfeature =  new GisFeature();
		} else  if ("fire_hydrant".equals(a)){
			gisfeature =  new FireStation();
		} else  if ("bicycle_rental".equals(a)){
			gisfeature =  new Rental();
		} else  if ("veterinary".equals(a)){
			gisfeature =  new Veterinary();
		} else  if ("residential".equals(a)){
			gisfeature =  new House();
		} else  if ("nursing_home".equals(a)){
			gisfeature =  new House();
		} else  if ("courthouse".equals(a)){
			gisfeature =  new CourtHouse();
		} else  if ("ferry_terminal".equals(a)){
			gisfeature =  new FerryTerminal();
		} else  if ("nightclub".equals(a)){
			gisfeature =  new NightClub();
		}  else  if ("arts_centre".equals(a)){
			gisfeature =  new Museum();
		} else  if ("bbq".equals(a)){
			gisfeature =  new GisFeature();
		} else  if ("parking_entrance".equals(a)){
			gisfeature =  new Parking();
		} else  if ("grit_bin".equals(a)){
			gisfeature =  new GisFeature();
		} else  if ("biergarten".equals(a)){
			gisfeature =  new Bar();
		} else  if ("car_rental".equals(a)){
			gisfeature =  new Rental();
		}  else  if ("clinic".equals(a)){
			gisfeature =  new Hospital();
		}  else  if ("prison".equals(a)){
			gisfeature =  new Prison();
		}  else  if ("car_sharing".equals(a)){
			gisfeature =  new Rental();
		}  else  if ("embassy".equals(a)){
			gisfeature =  new AdmBuilding();
		}  else  if ("driving_school".equals(a)){
			gisfeature =  new Shop();
		}  else  if ("ice_cream".equals(a)){
			gisfeature =  new Restaurant();
		}  else  if ("clock".equals(a)){
			gisfeature =  new GisFeature();
		}  else  if ("charging_station".equals(a)){
			gisfeature =  new GisFeature();
		}  else  if ("bureau_de_change".equals(a)){
			gisfeature =  new ATM();
		} else  if ("parking;fuel".equals(a)){
			gisfeature =  new Parking();
		} else  if ("shop".equals(a)){
			gisfeature =  new Shop();
		} else  if ("motorcycle_parking".equals(a)){
			gisfeature =  new Parking();
		} else  if ("casino".equals(a)){
			gisfeature =  new Casino();
		} else  if ("bus_stop".equals(a)){
			gisfeature =  new BusStation();
		} else  if ("building".equals(a)){
			gisfeature =  new Building();
		} else  if ("monastery".equals(a)){
			gisfeature =  new Religious();
		}  else {
			gisfeature =  new GisFeature();
		}
		gisfeature.setAmenity(amenity);
		gisfeature.setFeatureCode("OSM");
		gisfeature.setFeatureClass(gisfeature.getClass().getSimpleName().toUpperCase());
		return gisfeature;
	}

}
