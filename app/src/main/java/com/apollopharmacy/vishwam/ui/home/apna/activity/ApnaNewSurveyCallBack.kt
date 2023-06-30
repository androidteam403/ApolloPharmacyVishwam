package com.apollopharmacy.vishwam.ui.home.apna.activity

import com.apollopharmacy.vishwam.ui.home.apna.activity.model.*
import java.io.File

interface ApnaNewSurveyCallBack {
    fun onSelectState(position: Int, item: String, uid: String)

    fun onCityItemSelect(position: Int, item: String, uid: String)

    fun onOrganisedItemSelect(position: Int, item: String)

    fun onUnorganisedItemSelect(position: Int, item: String)

    fun onLocationListItemSelect(
        position: Int,
        location: String,
        state: String,
        city: String,
        locationUid: String,
        stateUid: String,
        cityUid: String,
    )

    fun onClickDeleteChemist(position: Int)

    fun onClickApartmentDelete(position: Int)

    fun onClickDeleteHospital(position: Int)

    fun onApartmentTypeItemSelect(position: Int, item: String)

    fun onTrafficStreetItemSelect(position: Int, item: String)

    fun onApnaSpecialityItemSelect(position: Int, uid: String, name: String)

    fun onClickTrafficGeneratorItemDelete(position: Int, deletedItem: String)

    fun onClickNeighbouringStoreDelete(position: Int)

    fun onTrafficGeneratorItemSelect(position: Int, item: String, selected: Boolean?)

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

//    fun onDataChanged(neighbouringList: ArrayList<NeighbouringLocationResponse.Data.ListData.Row>)

    fun onSuccessConnectToAzure(images: ArrayList<ImageDto>)

    fun onFailureConnectToAzure(message: String)

    fun onSuccessGetStateListApiCall(stateListResponse: StateListResponse)

    fun onFailureGetStateListApiCall(message: String)

    fun onSuccessGetCityListApiCall(cityListResponse: CityListResponse)

    fun onFailureGetCityListApiCall(message: String)

    fun onSuccessSurveyCreateApiCall(surveyCreateResponse: SurveyCreateResponse)

    fun onFailureSurveyCreateApiCall(message: String)

    fun onSuccessImagesConnectAzure(
        imageUrlList: List<SurveyCreateRequest.SiteImageMb.Image>,
        surveyCreateRequest: SurveyCreateRequest,
    )

    fun onFailureImageConnectAzure(message: String)

    fun onSuccessVideoConnectAzure(
        videoUrlList: List<SurveyCreateRequest.VideoMb.Video>,
        surveyCreateRequest: SurveyCreateRequest,
    )

    fun onFailureVideoConnectAzure(message: String)

    fun onSelectDimensionTypeItem(
        position: Int,
        item: String,
        dimenTypeSelectedItem: DimensionTypeResponse.Data.ListData.Row,
    )

    fun onSelectNeighbourLocation(position: Int, item: String)

    fun onSelectedAgeoftheBuildingMonth(month: String)

    fun onSuccessGetRegionListApiCall(regionListResponse: RegionListResponse)

    fun onFailureGetRegionListApiCall(message: String)

    fun onRegionSelect(regionName: String, regionUid: String, regionCode: String)
}