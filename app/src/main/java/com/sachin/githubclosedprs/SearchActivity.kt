package com.sachin.githubclosedprs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject

class SearchActivity : AppCompatActivity() {

    @Inject
    lateinit var mViewModelFactory: SearchViewModelFactory

    private val mViewModel: SearchViewModel by lazy {
        ViewModelProvider(
            this@SearchActivity,
            mViewModelFactory
        ).get(SearchViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        supportActionBar?.hide()

        DaggerCoreComponent.builder().coreModule(CoreModule(this@SearchActivity)).build()
            .injectSearchActivity(this@SearchActivity)

        setSearch()
    }

    private fun setSearch() {
        searchText.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mViewModel.getPullRequests(textView.text.toString())
                true;
            }
            false;
        }

        val adapter = PRAdapter()
        pullRequests.adapter = adapter
        val prList = mViewModel.getPullRequests(searchText.text.toString())
        prList.observe(this@SearchActivity, { pullRequestsList ->
            adapter.setPullRequestList(pullRequestsList)
        })
    }
}