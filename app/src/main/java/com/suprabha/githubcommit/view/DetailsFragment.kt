package com.suprabha.githubcommit.view

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.suprabha.githubcommit.R
import com.suprabha.githubcommit.api.CommitsApiResponse
import com.suprabha.githubcommit.api.GithubApiService
import com.suprabha.githubcommit.api.SpecificCommitResponse
import com.suprabha.githubcommit.databinding.FragmentDetailsBinding
import com.suprabha.githubcommit.databinding.FragmentListBinding
import com.suprabha.githubcommit.tools.Resource
import com.suprabha.githubcommit.tools.Status
import com.suprabha.githubcommit.viewmodel.CommitsViewModel
import com.suprabha.githubcommit.viewmodel.ViewModelFactory
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DetailsFragment : Fragment() {

    private lateinit var viewModelCommitDetails: CommitsViewModel
    private lateinit var binding: FragmentDetailsBinding
    private var commitDetailsObserver: Observer<Resource<SpecificCommitResponse>>? = null
    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentDetailsBinding.inflate(layoutInflater)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelCommitDetails= ViewModelProvider(this,
            ViewModelFactory(GithubApiService.create())).get(CommitsViewModel::class.java)

        commitDetailsObserver= Observer<Resource<SpecificCommitResponse>>{

            it?.let { resource: Resource<SpecificCommitResponse> ->

                when(resource.status){Status.SUCCESS->{

                    binding.textJust.visibility= View.GONE
                    binding.textAuthor.visibility= View.VISIBLE
                    binding.textDate.visibility= View.VISIBLE
                    binding.textSha.visibility= View.VISIBLE
                    binding.textMessage.visibility= View.VISIBLE
                    binding.textNumber.visibility= View.VISIBLE
                    binding.textTotal.visibility= View.VISIBLE
                    binding.textViewAuthorDetails.text= resource.data?.commit?.author?.name

                    val dateTime= ZonedDateTime.parse(resource.data?.commit?.author?.date)

                    binding.textViewDate.text= dateTime.withZoneSameInstant(ZoneId.of("IST"))
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a"))

                    binding.textViewSha.text= resource.data?.sha
                    binding.textViewMessageDetails.text= resource.data?.commit?.message
                    binding.textViewNumberFilesChanged.text= resource.data?.files?.size.toString()
                    binding.textViewTotalChanges.text= resource.data?.stats?.total.toString()
                } Status.ERROR-> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    Status.LOADING-> {}
                }}
        }
        viewModelCommitDetails.getSpecificCommit("square", "retrofit",
            args.argumentSha).observe(viewLifecycleOwner, commitDetailsObserver!!)
    }
}