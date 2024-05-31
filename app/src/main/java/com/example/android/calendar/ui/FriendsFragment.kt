package com.example.android.calendar.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.android.calendar.R
import com.example.android.calendar.databinding.FragmentFriendsBinding
import com.example.android.calendar.db.friend.FriendDb
import com.example.android.calendar.model.Friend
import com.example.android.calendar.ui.adapter.FriendAdapter

class FriendsFragment : BottomSheetDialogFragment() {

    private val viewModel: CalendarViewModel by activityViewModels()

    private var _binding: FragmentFriendsBinding? = null
    private val binding get() = _binding!!

    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAllUsers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFriendsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.closeButton.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.userLiveData.observe(viewLifecycleOwner){ user ->
            if(user != null){
                userId = user.id
                binding.firstFriendSwitch.isChecked = user.showToAllFriends
            }
        }

        viewModel.friendsLiveData.observe(viewLifecycleOwner){ friends ->
            setupAdapter(friends)
        }

        binding.firstFriendSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateShowToAllForUserEvents(isChecked)
            viewModel.updateUserShowToAllFriends(isChecked)
        }

    }

    private fun setupAdapter(friends: List<Friend>) {
        binding.friendsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.friendsRecyclerView.adapter = FriendAdapter(friends) { friend ->
            viewModel.addFriend(friend.id, userId){ success ->
                if (success) {
                    Toast.makeText(requireContext(), "Пользователь добавлен в друзья", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "Вы уже друзья", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}