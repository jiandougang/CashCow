package com.advertisement.cashcow.common.manager

import com.advertisement.cashcow.R
import com.advertisement.cashcow.common.base.BaseActivity
import com.advertisement.cashcow.module.login.password.LoginByPasswordFragment
import com.advertisement.cashcow.module.main.detail.DetailsFragment
import com.advertisement.cashcow.module.main.information.messagesCenter.MessagesCenterFragment
import com.advertisement.cashcow.module.main.information.subInformation.SubInformationFragment
import com.advertisement.cashcow.module.main.mine.cumulativeIncome.CumulativeIncomeFragment
import com.advertisement.cashcow.module.main.mine.myBankCard.MyBankCardsFragment
import com.advertisement.cashcow.module.main.mine.myCollection.MyCollectionFragment
import com.advertisement.cashcow.module.main.mine.other.CustomerServiceFragment
import com.advertisement.cashcow.module.main.mine.other.InviteFriendsFragment
import com.advertisement.cashcow.module.main.mine.other.WithdrawFragment
import com.advertisement.cashcow.module.main.mine.personalInformation.PersonalInformationFragment
import com.advertisement.cashcow.module.main.search.SearchFragment
import me.yokeyword.fragmentation.SupportFragment


/**
 * @author 吴国洪
 * created: 2018/07/05
 * desc:用于管理加载不同的Fragment,activity不作任何处理
 */
class EmptyActivity : BaseActivity() {
    private var supportFragment: SupportFragment? = null

    companion object {
        const val Activity_Key = "Activity_Key"//用于跳转不同Fragment

        const val DetailsFragment_Type = "DetailsFragment_Type"//根据资讯item类型跳转不同DetailFragment
        const val DetailsFragment_Advertisement_Id = "DetailsFragment_Advertisement_Id"


    }

    override fun layoutId(): Int {
        return R.layout.root_empty
    }


    override fun initData() {
        val intent = intent
        val activityKey = intent.getStringExtra(Activity_Key)

        when (activityKey) {
            DetailsFragment.javaClass.name -> {
                val informationBeanType = intent.getStringExtra(DetailsFragment_Type)
                val informationBeanAdvertisementId = intent.getStringExtra(DetailsFragment_Advertisement_Id)
                supportFragment = DetailsFragment.getInstance(informationBeanType, informationBeanAdvertisementId)
            }

            SubInformationFragment.javaClass.name -> {
                val subFragmentCategory = intent.getStringExtra(SubInformationFragment.Category)
                val subFragmentType = intent.getStringExtra(SubInformationFragment.Type)
                supportFragment = SubInformationFragment.getInstance(subFragmentType,subFragmentCategory,subFragmentType,"")
            }

            InviteFriendsFragment.javaClass.name -> {
                supportFragment = InviteFriendsFragment.getInstance()
            }

            CumulativeIncomeFragment.javaClass.name -> {
                val userId = intent.getStringExtra(CumulativeIncomeFragment.User_Id)
                supportFragment = CumulativeIncomeFragment.getInstance(userId)
            }

            LoginByPasswordFragment.javaClass.name -> {
                val finishForResult = intent.getStringExtra(LoginByPasswordFragment.Finish_Activity_For_Result)
                supportFragment = LoginByPasswordFragment.getInstance(finishForResult)
            }

            MyCollectionFragment.javaClass.name ->  {
                val subFragmentTitle = intent.getStringExtra(SubInformationFragment.Category)
                supportFragment = MyCollectionFragment.getInstance(subFragmentTitle)
            }

            CustomerServiceFragment.javaClass.name ->  {
                supportFragment = CustomerServiceFragment.getInstance()
            }

            MyBankCardsFragment.javaClass.name ->  {
                val subFragmentTitle = intent.getStringExtra(MyBankCardsFragment.Title)
                supportFragment = MyBankCardsFragment.getInstance(subFragmentTitle)
            }

            SearchFragment.javaClass.name ->  {
                supportFragment = SearchFragment.getInstance()
            }

            WithdrawFragment.javaClass.name ->  {
                supportFragment = WithdrawFragment.getInstance()
            }

            PersonalInformationFragment.javaClass.name ->  {
                val finishForResult = intent.getStringExtra(PersonalInformationFragment.Finish_Activity_For_Result)
                supportFragment = PersonalInformationFragment.getInstance(finishForResult)
            }

            MessagesCenterFragment.javaClass.name ->{
                supportFragment = MessagesCenterFragment.getInstance(getString(R.string.messages_center))
            }

        }
    }


    override fun initView() {
        loadRootFragment(R.id.fl_root_container, supportFragment!!)
    }

}


