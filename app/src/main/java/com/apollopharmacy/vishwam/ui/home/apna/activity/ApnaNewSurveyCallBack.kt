package com.apollopharmacy.vishwam.ui.home.apna.activity

import com.apollopharmacy.vishwam.ui.home.apna.activity.model.*
import java.io.File

interface ApnaNewSurveyCallBack {
    fun onOrganisedItemSelect(position: Int, item: String)

    fun onUnorganisedItemSelect(position: Int, item: String)

    fun onClickDeleteChemist(position: Int)

    fun onClickApartmentDelete(position: Int)

    fun onClickDeleteHospital(position: Int)

    fun onApartmentTypeItemSelect(position: Int, item: String)

    fun onTrafficStreetItemSelect(position: Int, item: String)

    fun onApnaSpecialityItemSelect(position: Int, item: String)

    fun onClickTrafficGeneratorItemDelete(position: Int)

    fun onTrafficGeneratorItemSelect(position: Int, item: String)

    fun deleteSiteImage(position: Int, file: File)

    fun previewImage(imageFile: File)

    fun onSuccessGetLocationListApiCall(locationListResponse: LocationListResponse)

    fun onFailureGetLocationListApiCall(message: String)

    fun onSuccessGetTrafficStreetTypeApiCall(trafficStreetTypeResponse: TrafficStreetTypeResponse)

    fun onFailureGetTrafficStreetTypeApiCall(message: String)

    fun onSuccessGetTrafficGeneratorsTypeApiCall(trafficGeneratorsResponse: TrafficGeneratorsResponse)

    fun onFailureGetTrafficGeneratorsTypeApiCall(message: String)

    fun onSuccessGetApartmentTypeApiCall(apartmentTypeResponse: ApartmentTypeResponse)

    fun onFailureGetApartmentTypeApiCall(message: String)

    fun onSuccessGetApnaSpecialityApiCall(apnaSpecialityResponse: ApnaSpecialityResponse)

    fun onFailureGetApnaSpecialityApiCall(message: String)

    fun onSuccessGetParkingTypeApiCall(parkingTypeResponse: ParkingTypeResponse)

    fun onFailureGetParkingTypeApiCall(message: String)

    fun onSuccessGetDimensionTypeApiCall(dimensionTypeResponse: DimensionTypeResponse)

    fun onFailureGetDimensionTypeApiCall(message: String)

    fun onSuccessGetNeighbouringLocationApiCall(neighbouringLocationResponse: NeighbouringLocationResponse)

    fun onFailureGetNeighbouringLocationApiCall(message: String)
}