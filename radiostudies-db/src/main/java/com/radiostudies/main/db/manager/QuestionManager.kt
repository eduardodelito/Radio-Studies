package com.radiostudies.main.db.manager

import com.radiostudies.main.db.dao.QuestionDao
import com.radiostudies.main.db.entity.QuestionEntity

/**
 * Created by eduardo.delito on 9/29/20.
 */
interface QuestionManager {
    fun insertQuestions(questions: List<QuestionEntity>)

    fun updateQuestion(question: String?, answers: List<String>?)

    fun querySelectedInitialQuestions(question: String?): List<String>?

    fun deleteQuestion()
}

class QuestionManagerImpl(private val questionDao: QuestionDao) : QuestionManager {

    override fun insertQuestions(questions: List<QuestionEntity>) {
        questionDao.deleteQuestion()
        questionDao.insertQuestion(questions)
    }

    override fun updateQuestion(question: String?, answers: List<String>?) {
        questionDao.update(question, answers)
    }

    override fun querySelectedInitialQuestions(question: String?) =
        questionDao.querySelectedIndex(question)

    override fun deleteQuestion() {
        questionDao.deleteQuestion()
    }
}
