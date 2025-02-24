package com.example.worka1.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worka1.R
import com.example.worka1.ServicesAdapter
import com.example.worka1.ui.home.components.HomeService
import com.example.worka1.databinding.FragmentHomeBinding
import com.example.worka1.ui.cart.CartActivity
import com.example.worka1.ui.home.components.ServiceCategories
import com.example.worka1.ui.location_manager.MapsActivity
import com.example.worka1.ui.search_services.SearchServicesActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // sets home services
        val servicesList = listOf(
            HomeService("carpenter", "Carpenter", R.drawable.carpenter, "Framework, roofing, partitions, scaffolding", listOf(
                ServiceCategories("clothes_hanger", "Clothes Hanger", R.drawable.hanger),
                ServiceCategories("bed", "Bed", R.drawable.bed),
                ServiceCategories("cupboard_drawer", "Cupboard and Drawer", R.drawable.cupboard),
                ServiceCategories("door", "Door", R.drawable.door),
                ServiceCategories("drill_hang", "Drill and Hang", R.drawable.drill),
                ServiceCategories("furniture_repair", "Furniture Repair", R.drawable.repair),
                ServiceCategories("windows_curtain", "Windows and Curtain", R.drawable.window),
                ServiceCategories("book_visit", "Book a visit", R.drawable.carpenter)
            )),

            HomeService("electrician", "Electrician", R.drawable.electrician, "Wiring, appliances, lighting, circuit repair", listOf(
                ServiceCategories("switch_socket", "Switch & Socket", R.drawable.socket),
                ServiceCategories("fan", "Fan", R.drawable.fan),
                ServiceCategories("wall_ceiling_light", "Wall/Ceiling Light", R.drawable.ceiling),
                ServiceCategories("wiring", "Wiring", R.drawable.wiring),
                ServiceCategories("doorbell", "Doorbell", R.drawable.doorbell),
                ServiceCategories("mcb_submeter", "MCB & Submeter", R.drawable.mcb),
                ServiceCategories("inverter_stabiliser", "Inverter & Stabiliser", R.drawable.inverter),
                ServiceCategories("appliance", "Appliance", R.drawable.appliance),
                ServiceCategories("book_visit", "Book a visit", R.drawable.electrician)
            )),

            HomeService("painter", "Painter", R.drawable.painter, "Wall painting, texture painting, waterproofing", listOf(
                ServiceCategories("interior_painting", "Interior Painting", R.drawable.interior),
                ServiceCategories("exterior_painting", "Exterior Painting", R.drawable.exterior),
                ServiceCategories("texture_painting", "Texture Painting", R.drawable.texture),
                ServiceCategories("waterproof_coating", "Waterproof Coating", R.drawable.waterproof),
                ServiceCategories("book_visit", "Book a visit", R.drawable.painter)
            )),

            HomeService("plumber", "Plumber", R.drawable.plumber, "Pipes, taps, leakage, drainage solutions", listOf(
                ServiceCategories("drainage_system", "Drainage System", R.drawable.drainag),
                ServiceCategories("bathroom_fittings", "Bathroom Fittings", R.drawable.bathroo),
                ServiceCategories("kitchen_fittings", "Kitchen Fittings", R.drawable.kitche),
                ServiceCategories("pipe_repairs", "Pipe Repairs", R.drawable.pip),
                ServiceCategories("drainage_cleaning", "Drainage Cleaning", R.drawable.drainclea),
                ServiceCategories("book_visit", "Book a visit", R.drawable.plumber)
            )),

            HomeService("cleaner", "Cleaner", R.drawable.cleaner, "Home deep cleaning, sofa cleaning, bathroom cleaning", listOf(
                ServiceCategories("bathroom_cleaning", "Bathroom Cleaning", R.drawable.bathroomclea),
                ServiceCategories("full_home_cleaning", "Full Home Cleaning", R.drawable.homeclea),
                ServiceCategories("kitchen_cleaning", "Kitchen Cleaning", R.drawable.kitchenclea),
                ServiceCategories("sofa_carpet_cleaning", "Sofa and Carpet Cleaning", R.drawable.furnitureclea),
                ServiceCategories("bug_control", "Bug Control", R.drawable.bugcontro)
            )),

            HomeService("home_decor", "Home Decor", R.drawable.homedecor, "Interior design, wallpaper, false ceiling", listOf(
                ServiceCategories("interior_decoration", "Interior Decoration", R.drawable.ic_back_arrow),
                ServiceCategories("waiter_booking", "Waiter Booking", R.drawable.ic_back_arrow),
                ServiceCategories("dj_booking", "DJ Booking", R.drawable.ic_back_arrow),
                ServiceCategories("party_events", "Party and Events", R.drawable.ic_back_arrow)
            )),

            HomeService("labour", "Labour", R.drawable.labour, "Mason, construction work, general labor", listOf(
                ServiceCategories("masonry_work", "Masonry Work", R.drawable.ic_back_arrow),
                ServiceCategories("labourer", "Labourer", R.drawable.ic_back_arrow),
                ServiceCategories("gardener", "Gardener", R.drawable.ic_back_arrow)
            )),

            HomeService("beautician", "Beautician", R.drawable.beautician, "Haircut, facial, bridal makeup, grooming", listOf(
                ServiceCategories("male_beautician", "Male", R.drawable.ic_back_arrow),
                ServiceCategories("female_beautician", "Female", R.drawable.ic_back_arrow)
            )),

            HomeService("A1_general_works", "A1 General Work", R.drawable.other, "Miscellaneous home services", listOf(
                ServiceCategories("home_tutor", "Home Tutor", R.drawable.ic_back_arrow),
                ServiceCategories("emergency_doctor_visit", "Emergency Doctor Visit", R.drawable.ic_back_arrow),
                ServiceCategories("mobile_repairing", "Mobile Repairing", R.drawable.ic_back_arrow),
                ServiceCategories("computer_repairing", "Computer Repairing", R.drawable.ic_back_arrow),
                ServiceCategories("mechanic", "Mechanic", R.drawable.ic_back_arrow),
                ServiceCategories("auto_rickshaw_driver", "Auto/Rickshaw Driver", R.drawable.ic_back_arrow),
                ServiceCategories("stitch_work", "Stitch Work", R.drawable.ic_back_arrow)
            ))
        )
        val servicesRecyclerView = binding.root.findViewById<RecyclerView>(R.id.services_container)
        val layoutManager = GridLayoutManager(requireContext(), 3)
        servicesRecyclerView.layoutManager = layoutManager
        servicesRecyclerView.adapter = ServicesAdapter(servicesList)

        // handles map navigation
        val locationIntent = Intent(requireContext(), MapsActivity::class.java)
        val locationContainer = view.findViewById<LinearLayout>(R.id.location_container_home)
        locationContainer.findViewById<LinearLayout>(R.id.locationLayout).setOnClickListener {
            startActivity(locationIntent)
        }

        // handles cart navigation
        val cartIntent = Intent(requireContext(), CartActivity::class.java)
        locationContainer.findViewById<LinearLayout>(R.id.cartButtonContainer).setOnClickListener {
            startActivity(cartIntent)
        }

        // search navigation container
        val searchContainer = view.findViewById<LinearLayout>(R.id.search_container_home)
        searchContainer.setOnClickListener{
            val searchIntent = Intent(requireContext(), SearchServicesActivity::class.java)
            startActivity(searchIntent)
        }

        // sets preview videos

        // sets trending, new services horizontal slider
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}