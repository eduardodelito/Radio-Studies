package com.radiostudies.main.ui.viewmodel

import com.radiostudies.main.common.livedata.SingleLiveEvent
import com.radiostudies.main.common.viewmodel.BaseViewModel
import com.radiostudies.main.db.entity.Diaries
import com.radiostudies.main.db.entity.Option
import com.radiostudies.main.db.manager.ActualManager
import com.radiostudies.main.db.model.Diary
import com.radiostudies.main.ui.mapper.diaryModelToDiaryEntity
import com.radiostudies.main.ui.model.diary.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AddDiaryViewModel @Inject constructor(private val actualManager: ActualManager) : BaseViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val diaryState = SingleLiveEvent<DiaryModelState>()
    internal fun getDiaryLiveData(): SingleLiveEvent<DiaryModelState> = diaryState

    var selectedTimeOfListeningList = mutableListOf<Option>()
    var selectedStations = mutableListOf<Option>()
    var selectedPlaceOfListening = mutableListOf<Option>()
    var selectedDevice = mutableListOf<Option>()

    var timeOfListeningList = mutableListOf<Option>()
    init {
        diaryState.postValue(AddDiaryForm(TIME_OF_LISTENING))
    }

    fun parseTimeOfListening(value: String?) {
        val jsonArray = JSONArray(value)
        for (i in 0 until jsonArray.length()) {
            val jObj = jsonArray.getJSONObject(i)
            val code = jObj.getString("code")
            val option = jObj.getString("value")
            timeOfListeningList.add(Option(code, option))
        }
    }

    fun queryStations(place: String) {
        launch {
            stations(place)
        }
    }

    private suspend fun stations(place: String) {
        withContext(Dispatchers.IO) {
            try {
                diaryState.postValue(StationsForm(actualManager.getSelectedArea(place)))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun placeOfListening() {
        val options = mutableListOf<Option>()
        options.add(Option("1", HOME))
        options.add(Option("2", WORK))
        options.add(Option("3", PRIVATE_VEHICLE))
        options.add(Option("4", BUS))
        options.add(Option("5", VAN_FX))
        options.add(Option("6", JEEPNEY))
        options.add(Option("7", TAXI))
        options.add(Option("8", OTHER_VEHICLE))
        options.add(Option("9", INTERNET_CAFE))
        options.add(Option("0", ELSEWHERE))
        diaryState.postValue(PlaceOfListeningForm(options))
    }

    fun device() {
        val options = mutableListOf<Option>()
        options.add(Option("A", RADIO_CASSETE))
        options.add(Option("B", STEREO))
        options.add(Option("C", KARAOKE))
        options.add(Option("D", DVD_CD))
        options.add(Option("E", CAR))
        options.add(Option("F", TRANSISTOR))
        options.add(Option("G", CELLPHONE))
        options.add(Option("H", MP3))
        options.add(Option("I", IPOD_IPAD))
        options.add(Option("J", INTERNET))
        options.add(Option("K", OTHERS))
        options.add(Option("L", NONE))
        options.add(Option("M", CABLE))
        diaryState.postValue(DeviceForm(options))
    }

    fun updateAndSaveDiary(diary: Diary) {
        launch {
            saveDiary(diary)
        }
    }

    private suspend fun saveDiary(diary: Diary) {
        withContext(Dispatchers.IO) {
            try {
                val diaries = Diaries(selectedTimeOfListeningList, selectedStations, selectedPlaceOfListening, selectedDevice)
                val diaryList = mutableListOf<Diaries>()
                diary.diaries?.forEach { value ->
                    val timeOfListening = value.timeOfListening
                    val radioStations = value.stations
                    val placeOfListening = value.placeOfListening
                    val devices = value.device
                    diaryList.add(Diaries(timeOfListening, radioStations, placeOfListening, devices))
                }
                diaryList.add(diaries)
                diary.diaries = diaryList
                actualManager.updateDiary(diary.diaryModelToDiaryEntity())
                diaryState.postValue(CompletedDiaryForm(true))
            } catch (e: Exception) {
                e.printStackTrace()
                diaryState.postValue(CompletedDiaryForm(false))
            }
        }
    }

    companion object {
        private const val TIME_OF_LISTENING = "time_of_listening.json"
        //Place of listening
        private const val HOME = "Home"
        private const val WORK = "Work"
        private const val PRIVATE_VEHICLE = "Private Vehicle"
        private const val BUS = "Bus"
        private const val VAN_FX = "Van/FX"
        private const val JEEPNEY = "Jeepney"
        private const val TAXI = "Taxi"
        private const val OTHER_VEHICLE = "Other Vehicles"
        private const val INTERNET_CAFE = "Internet Café"
        private const val ELSEWHERE = "Elsewhere"

        //Device
        private const val RADIO_CASSETE = "Radio Cassette Recorder"
        private const val STEREO = "Stereo Component"
        private const val KARAOKE = "Karaoke"
        private const val DVD_CD = "DVD/CD Players"
        private const val CAR = "Car Radio"
        private const val TRANSISTOR = "Transistor Radio"
        private const val CELLPHONE = "Cellphone"
        private const val MP3 = "MP3"
        private const val IPOD_IPAD = "iPod/iPad"
        private const val INTERNET = "Internet Streaming (PC/ Laptop)"
        private const val OTHERS = "Others"
        private const val NONE = "None"
        private const val CABLE = "Cable/TV with Radio"

    }
}