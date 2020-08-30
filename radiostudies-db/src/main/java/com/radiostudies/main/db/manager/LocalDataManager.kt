//package com.radiostudies.main.db.manager
//
//import com.radiostudies.main.db.model.Area
//import com.radiostudies.main.db.model.InitialQuestion
//import com.radiostudies.main.db.model.MainInfo
//import com.radiostudies.main.db.model.Question
//
///**
// * Created by eduardo.delito on 8/30/20.
// */
//interface LocalDataManager {
//
//    fun loadInitialQuestions()
//
//    fun loadMainInfo()
//
//    fun loadArea()
//
//    fun loadQuestion()
//}
//
//class LocalDataManagerImpl : LocalDataManager {
//
//    private fun options1(): List<String> {
//        val options = mutableListOf<String>()
//        options.add("AGREE")
//        options.add("DISAGREE")
//        return options
//    }
//
//    private fun options2(): List<String> {
//        val options = mutableListOf<String>()
//        options.add("Research Company")
//        options.add("TV Station")
//        options.add("Cable TV Station")
//        options.add("Radio Station")
//        options.add("Newspaper & Magazine")
//        options.add("Advertising Agencies")
//        options.add("Associations or Groups of People voluntarily serving media companies (TV/Radio/Newspaper/Magazines")
//        options.add("NONE OF THE ABOVE")
//        return options
//    }
//
//    private fun options3(): List<String> {
//        val options = mutableListOf<String>()
//        options.add("Yes")
//        options.add("No")
//        return options
//    }
//
//    override fun loadInitialQuestions() {
//        val initialQuestions = mutableListOf<InitialQuestion>()
//
//
//        initialQuestions.add(
//            InitialQuestion(
//                "Are you willing to participate in the study?",
//                options1(),
//                "Put Mobile Number"
//            )
//        )
//        initialQuestions.add(
//            InitialQuestion(
//                "Are you or any of your immediately family members / relatives / close friends currently working for the following types of companies?",
//                options2(),
//                ""
//            )
//        )
//        initialQuestions.add(
//            InitialQuestion(
//                "Have you or any of your immediately family members / relatives / close friends worked in the following types of companies for the past 6 months?",
//                options2(),
//                ""
//            )
//        )
//        initialQuestions.add(
//            InitialQuestion(
//                "Have you or any immediate member of your family participated in any form of TV or Radio Survey in your barangay within the last six (6) months?",
//                options3(),
//                "Specify"
//            )
//        )
//        initialQuestions.add(
//            InitialQuestion(
//                "Have you or any immediate member of your family participated in any promos in your barangay in the past six (6) months",
//                options3(),
//                "Specify"
//            )
//        )
//    }
//
//    override fun loadMainInfo() {
//        val mainInfo = mutableListOf<MainInfo>()
//    }
//
//    override fun loadArea() {
//        val areas = mutableListOf<Area>()
//        areas.add(Area("1", "Mega Manila"))
//        areas.add(Area("2", "Dagupan"))
//        areas.add(Area("3", "Baguio"))
//        areas.add(Area("4", "Batangas"))
//        areas.add(Area("5", "Naga"))
//        areas.add(Area("6", "Legazpi"))
//        areas.add(Area("7", "Tarlac"))
//        areas.add(Area("8", "Cabanatuan"))
//        areas.add(Area("9", "Puerto Princesa"))
//        areas.add(Area(":", "San Fernando"))
//        areas.add(Area(";", "Laoag"))
//        areas.add(Area("<", "Olongapo"))
//        areas.add(Area("=", "Angeles"))
//        areas.add(Area(">", "San Pablo"))
//        areas.add(Area("?", "Cebu"))
//        areas.add(Area("@", "Bacolod"))
//        areas.add(Area("A", "Iloilo"))
//        areas.add(Area("B", "Tacloban"))
//        areas.add(Area("C", "Vigan"))
//        areas.add(Area("D", "Tuguegarao"))
//        areas.add(Area("E", "Cauayan"))
//        areas.add(Area("F", "Cagayan De Oro"))
//        areas.add(Area("G", "Davao"))
//        areas.add(Area("H", "General Santos"))
//        areas.add(Area("I", "Santiago"))
//        areas.add(Area("J", "San Fernando, La Union"))
//        areas.add(Area("K", "Lecena"))
//        areas.add(Area("L", "Daet"))
//        areas.add(Area("M", "Sorsogon"))
//        areas.add(Area("N", "Dumaguete"))
//        areas.add(Area("O", "Kalibo"))
//        areas.add(Area("P", "Roxas"))
//        areas.add(Area("Q", "Tagbilaran"))
//        areas.add(Area("R", "Iligan"))
//        areas.add(Area("S", "Butuan"))
//        areas.add(Area("T", "Cotabato"))
//        areas.add(Area("U", "Surigao"))
//        areas.add(Area("V", "Koronadal"))
//        areas.add(Area("W", "Ozamis"))
//        areas.add(Area("X", "Pagadian"))
//        areas.add(Area("Y", "Malaybalay"))
//        areas.add(Area("Z", "Zamboanga"))
//        areas.add(Area("0", "NOT KNOWN/REFUSED"))
//        areas.add(Area("\\", "Rural Pangasinan"))
//        areas.add(Area("]", "Rural Cebu"))
//        areas.add(Area("^", "Rural Davao"))
//        areas.add(Area("h", "Metro Manila"))
//        areas.add(Area("i", "Tagum City"))
//        areas.add(Area("j", "Catanduanes"))
//        areas.add(Area("k", "Bayombong, Nueva Viscaya"))
//        areas.add(Area("l", "Kidapawan"))
//        areas.add(Area("m", "Valencia"))
//        areas.add(Area("n", "Dipolog"))
//    }
//
//    override fun loadQuestion() {
//        val questions = mutableListOf<Question>()
//    }
//}
