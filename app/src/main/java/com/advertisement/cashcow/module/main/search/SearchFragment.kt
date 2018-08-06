package com.advertisement.cashcow.module.main.search

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseFragment
import com.advertisement.cashcow.common.network.api.InformationApi
import com.advertisement.cashcow.common.network.bean.information.InformationAdApiBean
import com.advertisement.cashcow.common.network.bean.information.SearchHotTagApiBean
import com.advertisement.cashcow.module.main.information.InformationAdapter
import com.advertisement.cashcow.module.main.information.InformationBean
import com.advertisement.cashcow.module.main.information.InformationPresenter
import com.blankj.utilcode.util.StringUtils
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_search.*


/**
 * 作者：吴国洪 on 2018/6/04
 * 描述：搜索页面
 * 接口：
 */

class SearchFragment : BaseFragment(), View.OnClickListener, SearchContract.View {

    private var searchlRecyclerViewAdapter: LRecyclerViewAdapter? = null
    private var searchDatalRecyclerViewAdapter: LRecyclerViewAdapter? = null

    private var searchAdapter: SearchAdapter? = null
    private var searchDataAdapter: InformationAdapter? = null
    private lateinit var arrayData: ArrayList<SearchBean>
    private lateinit var arraySearchData: ArrayList<InformationBean>

    private var currentPage = 0


    private val mPresenter by lazy { SearchPresenter() }
    private val mSearchDataPresenter by lazy { InformationPresenter(SearchFragment.javaClass.name) }


    private val searchLinearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }
    private val searchDataLinearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    override fun getLayoutId(): Int = R.layout.fragment_search

    override fun handleSuccess(type: String, obj: Any) {
        when (type) {
            InformationApi.requestHotSearch -> {
                obj as SearchHotTagApiBean
                val position = 1//指定列表中的第0个item

                searchAdapter?.getDataList()!![position].tagTypeEntity = SearchBean.TagTypeItem(obj.resultData!!)

                searchlRecyclerViewAdapter?.notifyItemChanged(searchlRecyclerViewAdapter!!.getAdapterPosition(false, position), SearchBean.tagType)

            }

            InformationApi.requestQueryAds -> {
                if (currentPage == 0){
                    searchDataAdapter?.clear(0)
                }
                val informationApi = obj as InformationAdApiBean
                searchDataAdapter?.addAll(mSearchDataPresenter.parseAdData(informationApi.rows))

                if (informationApi.pageNo == informationApi.pages)
                    rv_search_data.setNoMore(true)

            }
        }
    }


    companion object {
        fun getInstance(): SearchFragment {
            val fragment = SearchFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView() {
        searchAdapter = SearchAdapter(activity!!, arrayData)
        searchAdapter!!.setClickListener(object: SearchAdapter.OnClickListener {
            override fun callBack(position: Int, text: String) {
                startSearchRequest(text)
                et_search.setText(text)
                et_search.setSelection(text.length)

            }
        })
        searchlRecyclerViewAdapter = LRecyclerViewAdapter(searchAdapter)
        searchDataAdapter = InformationAdapter(activity!!, arraySearchData)
        searchDatalRecyclerViewAdapter = LRecyclerViewAdapter(searchDataAdapter)

        rv_search.let {
            it.layoutManager = searchLinearLayoutManager
            it.adapter = searchlRecyclerViewAdapter
            it.itemAnimator = DefaultItemAnimator()
            it.setPullRefreshEnabled(false)
        }

        rv_search_data.let {
            it.layoutManager = searchDataLinearLayoutManager
            it.adapter = searchDatalRecyclerViewAdapter
            it.itemAnimator = DefaultItemAnimator()
            it.setPullRefreshEnabled(false)
            it.setOnLoadMoreListener {
                currentPage++
                mPresenter.requestqueryAds(context!!, et_search.text.toString(), currentPage.toString())

            }
        }
        et_search.let {
            it.setOnEditorActionListener { _, actionId, event ->

                //如果是按下，则响应，否则，一次按下会响应两次
                if (actionId == EditorInfo.IME_ACTION_SEND || event != null
                        && event.keyCode == KeyEvent.KEYCODE_ENTER &&
                        event.action == KeyEvent.ACTION_DOWN) {

                    val list = mPresenter.saveSearchTag(context!!, et_search.text.toString()) {
                        searchAdapter?.remove(3)
                    }
                    searchAdapter?.addAll(list)
                    startSearchRequest(et_search.text.toString())
                    true
                } else false
            }

            it.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (StringUtils.isEmpty(s)) {
                        ic_no_content.visibility = View.GONE
                        rv_search_data.visibility = View.GONE
                        rv_search.visibility = View.VISIBLE
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }

        iv_delete.setOnClickListener(this)
        tv_cancel.setOnClickListener(this)
    }

    fun startSearchRequest(key:String) {
        rv_search.visibility = View.GONE
        ic_no_content.visibility = View.GONE
        currentPage = 0
        mPresenter.requestqueryAds(context!!, key, currentPage.toString())
    }


    override fun lazyLoad() {
        mPresenter.attachView(this)
        arraySearchData = arrayListOf()
        arrayData = mPresenter.initAllData(context!!)
        mPresenter.requestHotSearchs()

    }

    override fun preLoad(type: String, obj: Any) {
        ic_mask.visibility = View.VISIBLE
        ic_no_content.visibility = View.GONE
    }

    override fun afterLoad(type: String, obj: Any) {
        ic_mask.visibility = View.GONE
        if (searchDataAdapter?.data?.size ?: 0 > 0) {
            rv_search_data.visibility = View.VISIBLE
            rv_search.visibility = View.GONE
            ic_no_content.visibility = View.GONE
        } else {
            rv_search_data.visibility = View.GONE
            rv_search.visibility = View.GONE
            ic_no_content.visibility = View.VISIBLE
        }
    }

    override fun handleError(type: String, obj: Any) {
    }

    override fun onClick(v: View?) {
        when {
            v?.id == R.id.iv_delete -> {
                et_search.text.clear()
            }

            v?.id == R.id.tv_cancel -> {
                activity!!.onBackPressed()
            }


        }
    }

}
