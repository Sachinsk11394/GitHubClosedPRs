package com.sachin.githubclosedprs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

open class SearchViewModel(private val searchRepository: SearchActivityRepository) : ViewModel() {
    private val pullRequests = MutableLiveData<ArrayList<PullRequest>>(arrayListOf())
    val error = MutableLiveData<String>()
    private val MAX_REPOS = 10

    fun getPullRequests(userName: String): LiveData<ArrayList<PullRequest>> {
        pullRequests.value?.clear()
        viewModelScope.launch {
            when (val reposResponse = searchRepository.getRepos(userName)) {
                is NetworkResponse.Success -> {
                    reposResponse.data.forEachIndexed { index, repo ->
                        if(index > MAX_REPOS) return@forEachIndexed
                        launch {
                            when (val pullRequestResponse = searchRepository.getClosedPRs(repo.owner.userName, repo.name)) {
                                is NetworkResponse.Success -> {
                                    pullRequests.value?.addAll(pullRequestResponse.data)
                                    pullRequests.notifyObserver()
                                }
                            }
                        }
                    }
                }
                is NetworkResponse.Failure -> {
                    error.postValue("Invalid user name")
                }
            }
        }
        return pullRequests
    }
}

fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}