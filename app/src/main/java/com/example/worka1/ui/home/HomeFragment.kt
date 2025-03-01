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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worka1.R
import com.example.worka1.ServicesAdapter
import com.example.worka1.ui.home.components.HomeService
import com.example.worka1.databinding.FragmentHomeBinding
import com.example.worka1.ui.cart.CartActivity
import com.example.worka1.ui.home.components.ServiceCategories
import com.example.worka1.ui.home.components.SubCategoryItem
import com.example.worka1.ui.home.components.SubCategoryItemAdapter
import com.example.worka1.ui.home.components.VideoAdapter
import com.example.worka1.ui.home.components.VideoItem
import com.example.worka1.ui.location_manager.MapsActivity
import com.example.worka1.ui.search_services.SearchServicesActivity
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.firestore

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
//        val servicesList = listOf(
//            HomeService("carpenter", "Carpenter", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_category/w_56,dpr_3,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/growth/home-screen/1678868062337-08bfc2.jpeg", "Framework, roofing, partitions, scaffolding", listOf(
//                ServiceCategories("clothes_hanger", "Clothes Hanger", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_72,dpr_2,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1726550805371-876b3c.jpeg"),
//                ServiceCategories("bed", "Bed", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_72,dpr_2,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1726507350527-c4abb0.jpeg"),
//                ServiceCategories("cupboard_drawer", "Cupboard and Drawer", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_72,dpr_2,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/growth/luminosity/1729154973527-106a70.jpeg"),
//                ServiceCategories("door", "Door", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_72,dpr_2,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1726507333153-653b92.jpeg"),
//                ServiceCategories("drill_hang", "Drill and Hang", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_72,dpr_2,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1726507335888-e0ff92.jpeg"),
//                ServiceCategories("furniture_repair", "Furniture Repair", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_72,dpr_2,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1726507348146-3823dd.jpeg"),
//                ServiceCategories("windows_curtain", "Windows and Curtain", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_72,dpr_2,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1726550802862-411248.jpeg"),
//                ServiceCategories("book_visit", "Book a visit", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_72,dpr_2,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1700638213050-c722c8.jpeg")
//            )),
//
//            HomeService("electrician", "Electrician", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_category/w_48,dpr_3,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/growth/luminosity/1699868754554-a5c5c9.jpeg", "Wiring, appliances, lighting, circuit repair", listOf(
//                ServiceCategories("switch_socket", "Switch & Socket", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_72,dpr_2,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1719646923803-15ecf2.jpeg"),
//                ServiceCategories("fan", "Fan", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_72,dpr_2,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/growth/luminosity/1729092471356-d358a6.jpeg"),
//                ServiceCategories("wall_ceiling_light", "Wall/Ceiling Light", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_72,dpr_2,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1700638215421-85b9d3.jpeg"),
//                ServiceCategories("wiring", "Wiring", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_72,dpr_2,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1701324401683-c49990.jpeg"),
//                ServiceCategories("doorbell", "Doorbell", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_72,dpr_2,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1719646953257-df29fe.jpeg"),
//                ServiceCategories("mcb_submeter", "MCB & Submeter", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_72,dpr_2,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1718988838289-6f4ca7.jpeg"),
//                ServiceCategories("inverter_stabiliser", "Inverter & Stabiliser", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_72,dpr_2,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1700638222320-fa8e14.jpeg"),
//                ServiceCategories("appliance", "Appliance", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_72,dpr_2,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1700638234862-fdd67a.jpeg"),
//                ServiceCategories("book_visit", "Book a visit", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_72,dpr_2,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1700638213050-c722c8.jpeg")
//            )),
//
//            HomeService("painter", "Painter", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_category/w_56,dpr_3,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/growth/home-screen/1674120935535-f8d5c8.jpeg", "Wall painting, texture painting, waterproofing", listOf(
//                ServiceCategories("interior_painting", "Interior Painting", "https://5.imimg.com/data5/SELLER/Default/2021/8/DC/WM/VI/136135105/interior-wall-painting-service-250x250.png"),
//                ServiceCategories("exterior_painting", "Exterior Painting", "https://thumbs.dreamstime.com/b/exterior-house-painting-5047589.jpg"),
//                ServiceCategories("texture_painting", "Texture Painting", "https://m.media-amazon.com/images/I/7121yUUL+6L._AC_UF350,350_QL80_.jpg"),
//                ServiceCategories("waterproof_coating", "Waterproof Coating", "https://www.triadbasementwaterproofing.com/wp-content/uploads/2023/10/foundation-waterproofing.jpg"),
//            )),
//
//            HomeService("plumber", "Plumber", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_category/w_48,dpr_3,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/growth/luminosity/1722431282836-ee5db3.jpeg", "Pipes, taps, leakage, drainage solutions", listOf(
//                ServiceCategories("bath_fittings", "Bath Fittings", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_72,dpr_2,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1726208784284-311057.jpeg"),
//                ServiceCategories("basin_sink", "Basin & Sink", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_72,dpr_2,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1726208786381-99c5c9.jpeg"),
//                ServiceCategories("grouting", "Grouting", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_72,dpr_2,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1726208788956-d88305.jpeg"),
//                ServiceCategories("water_filter", "Water Filter", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_72,dpr_2,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1726208791525-b93fc6.jpeg"),
//                ServiceCategories("drainage", "Drainage", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_72,dpr_2,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1726208793920-02e537.jpeg"),
//                ServiceCategories("toilet", "Toilet", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_72,dpr_2,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1726208800630-0a4b9f.jpeg"),
//                ServiceCategories("tap_mixer", "Tap & Mixer", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_72,dpr_2,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1726208807457-b543b1.jpeg"),
//                ServiceCategories("water_tank", "Water Tank", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_72,dpr_2,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1726208809699-7904d8.jpeg"),
//                ServiceCategories("motor", "Motor", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_72,dpr_2,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1726208812077-9d50b6.jpeg"),
//                ServiceCategories("water_pipes", "Water Pipes", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_72,dpr_2,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1726208814728-1aa5db.jpeg"),
//                ServiceCategories("book_visit", "Book a Visit", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_template,q_auto:low,f_auto/w_72,dpr_2,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1726208817629-0c5079.jpeg")
//            )),
//
//            HomeService("cleaner", "Cleaner", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_category/w_56,dpr_3,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/growth/home-screen/1681711961404-75dfec.jpeg", "Home deep cleaning, sofa cleaning, bathroom cleaning", listOf(
//                ServiceCategories("bathroom_cleaning", "Bathroom Cleaning", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_category/w_48,dpr_3,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/growth/luminosity/1728900642258-b12524.jpeg"),
//                ServiceCategories("full_home_cleaning", "Full Home Cleaning", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_category/w_48,dpr_3,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/growth/luminosity/1728900634115-e18640.jpeg"),
//                ServiceCategories("kitchen_cleaning", "Kitchen Cleaning", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_category/w_48,dpr_3,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/growth/luminosity/1728900636163-e44538.jpeg"),
//                ServiceCategories("sofa_carpet_cleaning", "Sofa and Carpet Cleaning", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_category/w_48,dpr_3,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/growth/luminosity/1728900640167-18109e.jpeg"),
//                ServiceCategories("bug_control", "Bug Control", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_category/w_48,dpr_3,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/growth/luminosity/1728900622918-72accc.jpeg")
//            )),
//
//            HomeService("home_decor", "Home Decor", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_category/w_56,dpr_3,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/growth/luminosity/1724138391296-c1780b.jpeg", "Interior design, wallpaper, false ceiling", listOf(
//                ServiceCategories("interior_decoration", "Interior Decoration", "https://e7.pngegg.com/pngimages/976/775/png-clipart-blue-suede-3-seat-sofa-and-gray-sofa-chair-interior-design-services-poster-continental-lite-interior-design-angle-furniture.png"),
//                ServiceCategories("waiter_booking", "Waiter Booking", "https://w7.pngwing.com/pngs/350/812/png-transparent-waiter-tray-t-shirt-graphy-table-the-waiter-furniture-plate-public-relations-thumbnail.png"),
//                ServiceCategories("dj_booking", "DJ Booking", "https://pngimg.com/d/dj_PNG43.png"),
//                ServiceCategories("party_events", "Party and Events", "https://w7.pngwing.com/pngs/644/545/png-transparent-silhouette-party-people-graduation-season-silhouette-of-people-dancing-purple-other-text-thumbnail.png")
//            )),
//
//            HomeService("labour", "Labour", "https://cdn-icons-png.flaticon.com/256/11790/11790858.png", "Mason, construction work, general labor", listOf(
//                ServiceCategories("masonry_work", "Masonry Work", "https://t3.ftcdn.net/jpg/05/43/25/20/240_F_543252012_WRGkhpOMBdmErNPJ8dyxgUZiCiql2JVV.png"),
//                ServiceCategories("labourer", "Labourer", "https://cdn-icons-png.flaticon.com/256/11790/11790858.png"),
//                ServiceCategories("gardener", "Gardener", "https://images.rawpixel.com/image_png_800/cHJpdmF0ZS9sci9pbWFnZXMvd2Vic2l0ZS8yMDIzLTA5L3Jhd3BpeGVsb2ZmaWNlMTRfcGhvdG9fb2ZfNDBzX2FzaWFuX21hbl9nYXJkZW5lcl9ob2xkaW5nX3BvdHRlZF8yOWJjZjg0Ny0zMmU2LTRmYzQtYmU2MC1lOTcwYTgxNTc1OGJfMS5wbmc.png")
//            )),
//
//            HomeService("beautician", "Beautician", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_category/w_56,dpr_3,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1678864013225-bfc1de.jpeg", "Haircut, facial, bridal makeup, grooming", listOf(
//                ServiceCategories("male_beautician", "Male", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_category/w_56,dpr_3,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/growth/luminosity/1710241114433-5cfa7c.jpeg"),
//                ServiceCategories("female_beautician", "Female", "https://res.cloudinary.com/urbanclap/image/upload/t_high_res_category/w_56,dpr_3,fl_progressive:steep,q_auto:low,f_auto,c_limit/images/supply/customer-app-supply/1678864013225-bfc1de.jpeg")
//            )),
//
//            HomeService("A1_general_works", "A1 General Work", "https://cdn-icons-png.flaticon.com/256/8644/8644377.png", "Miscellaneous home services", listOf(
//                ServiceCategories("home_tutor", "Home Tutor", "https://e7.pngegg.com/pngimages/202/169/png-clipart-juku-student-in-home-tutoring-teacher-%E6%8C%87%E5%B0%8E-autistic-spectrum-disorders-child-reading.png"),
//                ServiceCategories("emergency_doctor_visit", "Emergency Doctor Visit", "https://www.pngfind.com/pngs/m/53-531148_black-doctor-png-black-medical-doctor-png-transparent.png"),
//                ServiceCategories("mobile_repairing", "Mobile Repairing", "https://png.pngtree.com/png-clipart/20201209/original/pngtree-repair-mobile-phone-png-image_5632579.jpg"),
//                ServiceCategories("computer_repairing", "Computer Repairing", "https://e7.pngegg.com/pngimages/471/66/png-clipart-computer-hardware-computer-repair-technician-computer-scientist-electronics-computer-computer-network-photography.png"),
//                ServiceCategories("mechanic", "Mechanic", "https://img.favpng.com/10/19/23/car-daihatsu-automobile-repair-shop-auto-mechanic-png-favpng-waNABn5tuZny0PwMpMg2h9Wad.jpg"),
//                ServiceCategories("auto_rickshaw_driver", "Auto/Rickshaw Driver", "https://w7.pngwing.com/pngs/961/502/png-transparent-scooter-car-auto-rickshaw-compact-van-scooter-van-scooter-diesel-fuel-thumbnail.png"),
//                ServiceCategories("stitch_work", "Stitch Work", "https://png.pngtree.com/png-vector/20190119/ourmid/pngtree-tailor-cartoon-cartoon-tailor-lovely-png-image_479114.png")
//            ))
//        )
//        val firestore = Firebase.firestore
//        val servicesCollection = firestore.collection("services")
//
//        for (service in servicesList) {
//            servicesCollection.document(service.id).set(service)
//        }
//
//        val servicesRecyclerView = binding.root.findViewById<RecyclerView>(R.id.services_container)
//        val layoutManager = GridLayoutManager(requireContext(), 3)
//        servicesRecyclerView.layoutManager = layoutManager
//        servicesRecyclerView.adapter = ServicesAdapter(servicesList)

        val firestore = Firebase.firestore
        val servicesCollection = firestore.collection("services")

        val servicesRecyclerView = binding.root.findViewById<RecyclerView>(R.id.services_container)
        val layoutManager = GridLayoutManager(requireContext(), 3)
        servicesRecyclerView.layoutManager = layoutManager

        val servicesList = mutableListOf<HomeService>()
        val servicesAdapter = ServicesAdapter(servicesList)
        servicesRecyclerView.adapter = servicesAdapter

        // fetch data from Firestore
        servicesCollection.get()
            .addOnSuccessListener { documents ->
                servicesList.clear()
                for (document in documents) {
                    val service = document.toObject(HomeService::class.java)
                    servicesList.add(service)
                }
                servicesAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
            }


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
        val videoList = listOf(
            VideoItem("https://media.giphy.com/media/kJEK2i63DXne04EYJh/giphy.gif", "Trending Now"),
            VideoItem("https://media.giphy.com/media/f9p9VjrLWO5ag/giphy.gif", "Popular Services"),
            VideoItem("https://media.giphy.com/media/xT9IgzoKnwFNmISR8I/giphy.gif", "Best Rated"),
        )

        val feedbackRecyclerView = binding.root.findViewById<RecyclerView>(R.id.feedbackPreviews)
        feedbackRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        feedbackRecyclerView.adapter = VideoAdapter(requireContext(), videoList)

        // sets new services horizontal slider
        val newSubCategoriesList = listOf(
            SubCategoryItem("1", "electrician", "switch_socket", "Smart Lights", R.drawable.plumber, 4.5f, 120, "₹1,299"),
            SubCategoryItem("2", "carpenter", "drill_hang", "Roofing", R.drawable.carpenter, 4.2f, 85, "₹2,499"),
            SubCategoryItem("3", "cleaner", "bathroom_fittings", "Bathroom Cleaning", R.drawable.cleaner, 4.8f, 230, "₹3,799"),
            SubCategoryItem("4", "plumber", "drainage_system", "Drainage System", R.drawable.plumber, 4.6f, 150, "₹4,999"),
            SubCategoryItem("5", "painter", "interior_painting", "Interior Painting", R.drawable.painter, 4.3f, 95, "₹899")
        )

        val newSubCategoriesRecyclerView = binding.root.findViewById<RecyclerView>(R.id.newSubCategories)
        newSubCategoriesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        newSubCategoriesRecyclerView.adapter = SubCategoryItemAdapter(requireContext(), newSubCategoriesList)

        //  sets most booked services horizontal slider
        val mostBookedSubCategoryList = listOf(
            // new items
            SubCategoryItem("1", "labour", "masonry_work", "Masonry Work", R.drawable.labour, 4.5f, 120, "₹1,299"),
            SubCategoryItem("2", "beautician", "male_beautician", "Male Beautician", R.drawable.beautician, 4.2f, 85, "₹2,499"),
            SubCategoryItem("3", "home_decor", "interior_decoration", "Interior Decoration", R.drawable.homedecor, 4.8f, 230, "₹3,799"),
            SubCategoryItem("4", "cleaner", "bathroom_cleaning", "Bathroom Cleaning", R.drawable.cleaner, 4.6f, 150, "₹4,999"),
            SubCategoryItem("5", "carpenter", "roofing", "Roofing", R.drawable.carpenter, 4.3f, 95, "₹899")
        )

        val mostBookedSubCategoryRecyclerView = binding.root.findViewById<RecyclerView>(R.id.mostBookedSubCategories)
        mostBookedSubCategoryRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        mostBookedSubCategoryRecyclerView.adapter = SubCategoryItemAdapter(requireContext(), mostBookedSubCategoryList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}