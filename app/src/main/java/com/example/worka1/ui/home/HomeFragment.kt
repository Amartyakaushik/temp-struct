package com.example.worka1.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worka1.R
import com.example.worka1.ServicesAdapter
import com.example.worka1.ui.home.components.HomeService
import com.example.worka1.databinding.FragmentHomeBinding
import com.example.worka1.ui.home.components.ServiceCategories

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val servicesList = listOf(
            HomeService("Carpenter", R.drawable.carpenter, "Framework, roofing, partitions, scaffolding", listOf(
                ServiceCategories("Clothes Hanger", R.drawable.hanger),
                ServiceCategories("Bed", R.drawable.bed),
                ServiceCategories("Cupboard and Drawer", R.drawable.cupboard),
                ServiceCategories("Door", R.drawable.door),
                ServiceCategories("Drill and Hang", R.drawable.drill),
                ServiceCategories("Furniture Repair", R.drawable.repair),
                ServiceCategories("Windows and Curtain", R.drawable.window),
                ServiceCategories("Book a visit", R.drawable.carpenter),
            )),

            HomeService("Electrician", R.drawable.electrician, "Wiring, appliances, lighting, circuit repair", listOf(
                ServiceCategories("Switch & Socket", R.drawable.socket),
                ServiceCategories("Fan", R.drawable.fan),
                ServiceCategories("Wall/ceiling light", R.drawable.ceiling),
                ServiceCategories("Wiring", R.drawable.wiring),
                ServiceCategories("Doorbell", R.drawable.doorbell),
                ServiceCategories("MCB & submeter", R.drawable.mcb),
                ServiceCategories("Inverter & stabliser", R.drawable.inverter),
                ServiceCategories("Appliance", R.drawable.appliance),
                ServiceCategories("Book a visit", R.drawable.electrician),
            )),
            HomeService("Painter", R.drawable.painter, "Wall painting, texture painting, waterproofing", listOf(
                ServiceCategories("Interior Painting", R.drawable.interior),
                ServiceCategories("Exterior Painting", R.drawable.exterior),
                ServiceCategories("Texture Painting", R.drawable.texture),
                ServiceCategories("Waterproof Coating", R.drawable.waterproof),
                ServiceCategories("Book a visit", R.drawable.painter),
            )),
            HomeService("Plumber", R.drawable.plumber, "Pipes, taps, leakage, drainage solutions", listOf(
                ServiceCategories("Drainage System", R.drawable.drainag),
                ServiceCategories("Bathroom Fittings", R.drawable.bathroo),
                ServiceCategories("Kitchen Fittings", R.drawable.kitche),
                ServiceCategories("Pipe Repairs", R.drawable.pip),
                ServiceCategories("Drainage Cleaning", R.drawable.drainclea),
                ServiceCategories("Book a visit", R.drawable.plumber),
            )),
            HomeService("Cleaner", R.drawable.cleaner, "Home deep cleaning, sofa cleaning, bathroom cleaning", listOf(
                ServiceCategories("Bathroom Cleaning", R.drawable.bathroomclea),
                ServiceCategories("Full Home Cleaning", R.drawable.homeclea),
                ServiceCategories("Kitchen Cleaning", R.drawable.kitchenclea),
                ServiceCategories("Sofa and Carpet Cleaning", R.drawable.furnitureclea),
                ServiceCategories("Bug control", R.drawable.bugcontro),
            )),
            HomeService("Home Decor", R.drawable.homedecor, "Interior design, wallpaper, false ceiling", listOf(
                ServiceCategories("Interior decoration", R.drawable.ic_back_arrow),
                ServiceCategories("Waiter booking", R.drawable.ic_back_arrow),
                ServiceCategories("DJ booking", R.drawable.ic_back_arrow),
                ServiceCategories("Party and events", R.drawable.ic_back_arrow)
            )),
            HomeService("Labour", R.drawable.labour, "Mason, construction work, general labor", listOf(
                ServiceCategories("Masonry Work", R.drawable.ic_back_arrow),
                ServiceCategories("Labourer", R.drawable.ic_back_arrow),
                ServiceCategories("Gardener", R.drawable.ic_back_arrow)
            )),
            HomeService("Beautician", R.drawable.beautician, "Haircut, facial, bridal makeup, grooming", listOf(
                ServiceCategories("Male", R.drawable.ic_back_arrow),
                ServiceCategories("Female", R.drawable.ic_back_arrow),
            )),
            HomeService("A1 General Work", R.drawable.other, "Miscellaneous home services", listOf(
                ServiceCategories("Home Tutor", R.drawable.ic_back_arrow),
                ServiceCategories("Emergency doctor visit", R.drawable.ic_back_arrow),
                ServiceCategories("Mobile repairing", R.drawable.ic_back_arrow),
                ServiceCategories("Computer repairing", R.drawable.ic_back_arrow),
                ServiceCategories("Mechanic", R.drawable.ic_back_arrow),
                ServiceCategories("Auto/Rickshaw driver", R.drawable.ic_back_arrow),
                ServiceCategories("Stitch work", R.drawable.ic_back_arrow)
            ))
        )
        val servicesRecyclerView = binding.root.findViewById<RecyclerView>(R.id.services_container)
        val layoutManager = GridLayoutManager(requireContext(), 3)
        servicesRecyclerView.layoutManager = layoutManager
        servicesRecyclerView.adapter = ServicesAdapter(servicesList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}