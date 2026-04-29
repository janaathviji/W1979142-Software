// Janaath Vijithavarnan
// W1979142

// This is the database of the disease
// This is a static data source that stores structured medical information for each skin condition
// Each function returns a fully populated DiseaseInformation object,
// which is used by the UI to display details and by the classifier to map predictions to readable content


package com.example.dermoinspect.data

import com.example.dermoinspect.data.model.*

object DiseaseDatabase {

    // Returns a list of all supported skin diseases in the app
    fun getAllDiseases(): List<DiseaseInformation> {
        return listOf(
            getMelanomaInfo(),
            getBasalCellCarcinomaInfo(),
            getMelanocyticNeviInfo(),
            getBenignKeratosisInfo(),
            getActinicKeratosesInfo(),
            getDermatofibromaInfo(),
            getVascularLesionsInfo()
        )
    }

    // Returns detailed information for Melanoma
    private fun getMelanomaInfo() = DiseaseInformation(
        name = "Melanoma",
        shortName = "Melanoma",
        isMalignant = true,
        category = DiseaseCategory.MALIGNANT,
        shortDescription = "Most serious type of skin cancer requiring early detection and treatment",
        overview = """
            Melanoma is the most serious type of skin cancer. It develops in melanocytes, the cells that produce melanin (the pigment that gives skin its color).
            
            While melanoma is less common than other skin cancers, it's more dangerous because it's more likely to spread to other parts of the body if not caught early.
            
            The good news: When detected and treated early, melanoma has a very high cure rate. This is why regular skin checks and awareness of warning signs are crucial.
        """.trimIndent(),
        symptoms = listOf(
            Symptom(
                "A - Asymmetry",
                "One half of the mole doesn't match the other half in shape, color, or thickness"
            ),
            Symptom(
                "B - Border Irregularity",
                "The edges are ragged, notched, blurred, or irregular rather than smooth"
            ),
            Symptom(
                "C - Color Variation",
                "Multiple colors present including black, brown, tan, red, white, or blue"
            ),
            Symptom(
                "D - Diameter",
                "Larger than 6mm (about the size of a pencil eraser), though melanomas can be smaller"
            ),
            Symptom(
                "E - Evolving",
                "Changes in size, shape, color, elevation, or symptoms like bleeding, itching, or crusting"
            )
        ),
        causes = listOf(
            "Ultraviolet (UV) radiation from sun exposure",
            "UV radiation from tanning beds",
            "Severe sunburns, especially in childhood",
            "Genetic mutations in skin cells"
        ),
        riskFactors = listOf(
            "Fair skin, light hair, and light eyes",
            "Family history of melanoma",
            "Personal history of melanoma or other skin cancers",
            "Many moles (more than 50) or atypical moles",
            "Weakened immune system",
            "Age (risk increases with age)",
            "History of severe sunburns"
        ),
        prevention = listOf(
            "Apply broad-spectrum SPF 30+ sunscreen daily, reapply every 2 hours",
            "Seek shade during peak sun hours (10 AM - 4 PM)",
            "Wear protective clothing including wide-brimmed hats",
            "Avoid tanning beds completely",
            "Perform monthly self-examinations of your skin",
            "Get annual professional skin exams",
            "Protect children from sun exposure and sunburns"
        ),
        treatment = listOf(
            "Surgical excision (removing the melanoma with surrounding margin)",
            "Sentinel lymph node biopsy for staging",
            "Immunotherapy to boost immune system response",
            "Targeted therapy for specific genetic mutations",
            "Radiation therapy in some cases",
            "Chemotherapy for advanced cases"
        ),
        whenToSeeDoctor = listOf(
            "New mole appears after age 30",
            "Existing mole shows any ABCDE warning signs",
            "Mole bleeds, itches, or becomes painful",
            "You have a family history of melanoma",
            "You notice any unusual skin changes"
        ),
        prognosis = "5-year survival rates: Stage 0-I: 99%, Stage II: 65-90%, Stage III: 40-70%, Stage IV: 10-30%. Early detection is crucial for best outcomes.",
        faqs = listOf(
            FAQ(
                "Can melanoma be cured?",
                "Yes, when caught early (Stage 0-I), melanoma has a 99% cure rate. This is why regular skin checks and early detection are so important."
            ),
            FAQ(
                "Is melanoma hereditary?",
                "About 10% of melanoma cases run in families. If you have a close relative with melanoma, your risk increases 2-3 times."
            ),
            FAQ(
                "Can melanoma appear on areas not exposed to sun?",
                "Yes, melanoma can develop anywhere on the body, including areas rarely exposed to sun like palms, soles, and under nails."
            )
        )
    )

    // Returns detailed information for Basal Cell Carcinoma
    private fun getBasalCellCarcinomaInfo() = DiseaseInformation(
        name = "Basal Cell Carcinoma",
        shortName = "BCC",
        isMalignant = true,
        category = DiseaseCategory.MALIGNANT,
        shortDescription = "Most common skin cancer that grows slowly and rarely spreads",
        overview = """
            Basal cell carcinoma (BCC) is the most common type of skin cancer. It develops in the basal cells, which are found at the bottom of the epidermis (outer layer of skin).
            
            BCC grows slowly and rarely spreads to other parts of the body, making it highly treatable. However, if left untreated, it can grow deep into the skin and damage surrounding tissue, nerves, and bones.
            
            Most BCCs occur on areas frequently exposed to sun, especially the face, ears, neck, scalp, shoulders, and back.
        """.trimIndent(),
        symptoms = listOf(
            Symptom(
                "Pearly or Waxy Bump",
                "A shiny, dome-shaped bump that may have visible blood vessels"
            ),
            Symptom(
                "Flat, Scaly Patch",
                "A flat, flesh-colored or brown scar-like lesion"
            ),
            Symptom(
                "Bleeding or Oozing Sore",
                "A sore that bleeds easily, doesn't heal, or heals and returns"
            ),
            Symptom(
                "Pink Growth",
                "A slightly raised pink growth with a crusted center or indentation"
            )
        ),
        causes = listOf(
            "Long-term exposure to UV radiation from sun",
            "UV radiation from tanning beds",
            "Cumulative sun damage over years",
            "Radiation therapy treatment"
        ),
        riskFactors = listOf(
            "Fair skin, light hair, and light eyes",
            "Chronic sun exposure over lifetime",
            "History of severe sunburns",
            "Age (more common in people over 50)",
            "Male gender (twice as common in men)",
            "Family history of skin cancer",
            "Weakened immune system"
        ),
        prevention = listOf(
            "Daily use of broad-spectrum SPF 30+ sunscreen",
            "Avoid peak sun hours (10 AM - 4 PM)",
            "Wear protective clothing and wide-brimmed hats",
            "Never use tanning beds",
            "Check your skin monthly for changes",
            "Get annual skin exams from a dermatologist"
        ),
        treatment = listOf(
            "Mohs surgery (most effective, removes cancer layer by layer)",
            "Surgical excision (cutting out the cancer)",
            "Curettage and electrodesiccation (scraping and burning)",
            "Cryotherapy (freezing with liquid nitrogen)",
            "Topical chemotherapy creams for superficial BCCs",
            "Radiation therapy when surgery isn't suitable"
        ),
        whenToSeeDoctor = listOf(
            "New growth or sore that doesn't heal within 4 weeks",
            "Bump that bleeds easily when touched",
            "Existing spot changes in appearance",
            "Persistent itching or pain in a skin spot",
            "You have risk factors and notice any skin changes"
        ),
        prognosis = "BCC has nearly a 100% cure rate when detected and treated early. However, 50% of patients who've had one BCC will develop another within 5 years, so ongoing monitoring is essential.",
        faqs = listOf(
            FAQ(
                "Does BCC spread to other organs?",
                "BCC rarely spreads (metastasizes) to other parts of the body. However, if left untreated, it can grow deep and cause significant local damage."
            ),
            FAQ(
                "Will I get more BCCs after treatment?",
                "About 50% of people who've had one BCC will develop another within 5 years. Regular skin checks and sun protection are crucial."
            ),
            FAQ(
                "Can BCC come back after removal?",
                "Yes, BCC can recur in the same location, especially if not completely removed. Mohs surgery has the lowest recurrence rate at 1-2%."
            )
        )
    )

    // Returns detailed information for Melanocytic Nevi

    private fun getMelanocyticNeviInfo() = DiseaseInformation(
        name = "Melanocytic Nevi (Moles)",
        shortName = "Moles",
        isMalignant = false,
        category = DiseaseCategory.BENIGN,
        shortDescription = "Common benign skin growths that should be monitored for changes",
        overview = """
            Melanocytic nevi, commonly called moles, are benign (non-cancerous) skin growths made up of melanocytes (pigment-producing cells). Most people have between 10-40 moles on their body.
            
            The vast majority of moles are completely harmless and never cause problems. However, having many moles (especially atypical moles) can increase the risk of developing melanoma.
            
            While most moles appear during childhood and young adulthood, it's important to monitor existing moles and be aware of any new moles that appear after age 30.
        """.trimIndent(),
        symptoms = listOf(
            Symptom(
                "Uniform Color",
                "Usually one color throughout - brown, black, tan, pink, or flesh-colored"
            ),
            Symptom(
                "Round or Oval Shape",
                "Regular, symmetrical shape with smooth, well-defined borders"
            ),
            Symptom(
                "Consistent Size",
                "Typically smaller than 6mm (pencil eraser), though can be larger"
            ),
            Symptom(
                "Stable Appearance",
                "Doesn't change significantly in size, shape, or color over time"
            )
        ),
        causes = listOf(
            "Genetic factors (inherited tendency to develop moles)",
            "Sun exposure (UV radiation stimulates melanocyte growth)",
            "Hormonal changes (pregnancy, puberty)",
            "Normal skin development"
        ),
        riskFactors = listOf(
            "Fair skin that burns easily",
            "Family history of many moles",
            "Sun exposure, especially in childhood",
            "Genetic factors",
            "Hormonal changes"
        ),
        prevention = listOf(
            "Protect skin from sun with SPF 30+ sunscreen",
            "Avoid tanning beds",
            "Wear protective clothing in sun",
            "Monitor existing moles for changes",
            "Perform monthly self-skin exams",
            "Get annual professional skin exams if you have many moles"
        ),
        treatment = listOf(
            "Most moles require no treatment",
            "Surgical removal if mole is suspicious or bothersome",
            "Shave excision for raised moles",
            "Surgical excision for flat moles or if cancer is suspected",
            "Moles should not be removed at home"
        ),
        whenToSeeDoctor = listOf(
            "Mole shows any ABCDE warning signs",
            "New mole appears after age 30",
            "Mole bleeds, itches, or becomes painful",
            "Mole changes in any way",
            "You have more than 50 moles total"
        ),
        prognosis = "Benign moles are harmless and cause no health problems. However, regular monitoring is important as changes could indicate melanoma development.",
        faqs = listOf(
            FAQ(
                "Are all moles harmless?",
                "Most moles are benign and harmless. However, any mole that changes or shows ABCDE warning signs should be evaluated by a doctor."
            ),
            FAQ(
                "Can moles turn into melanoma?",
                "While most melanomas arise in normal skin, about 20-30% develop from existing moles. This is why monitoring moles is important."
            ),
            FAQ(
                "Should I remove my moles?",
                "Moles only need removal if they're suspicious for cancer, irritated by clothing, or for cosmetic reasons. Never remove moles at home."
            )
        )
    )

    // Returns detailed information for Benign Keratosis
    private fun getBenignKeratosisInfo() = DiseaseInformation(
        name = "Benign Keratosis-like Lesions",
        shortName = "Seborrheic Keratosis",
        isMalignant = false,
        category = DiseaseCategory.BENIGN,
        shortDescription = "Non-cancerous skin growths that appear with age",
        overview = """
            Benign keratosis-like lesions, often called seborrheic keratoses, are harmless, non-cancerous skin growths that become more common as people age. They're sometimes called "age spots" or "wisdom warts."
            
            These growths appear as if they've been stuck onto the skin's surface and can range from light tan to black in color. While they may look concerning, they're completely harmless and never become cancerous.
            
            Most people will develop at least one seborrheic keratosis in their lifetime, typically appearing after age 40.
        """.trimIndent(),
        symptoms = listOf(
            Symptom(
                "Waxy, Stuck-on Appearance",
                "Looks like it was dripped onto the skin and can be easily picked off (but don't!)"
            ),
            Symptom(
                "Varied Colors",
                "Can be light tan, brown, black, or sometimes pink"
            ),
            Symptom(
                "Slightly Raised",
                "Elevated above skin surface with rough, warty texture"
            ),
            Symptom(
                "Various Sizes",
                "Can range from very small to over 1 inch in diameter"
            )
        ),
        causes = listOf(
            "Age-related changes in skin",
            "Genetic predisposition",
            "Sun exposure may play a role",
            "Normal part of skin aging process"
        ),
        riskFactors = listOf(
            "Age (more common after 40)",
            "Family history",
            "Fair skin",
            "Sun exposure history"
        ),
        prevention = listOf(
            "No proven prevention methods",
            "Sun protection may reduce numbers",
            "Regular skin monitoring",
            "These are a normal part of aging"
        ),
        treatment = listOf(
            "No treatment necessary (these are harmless)",
            "Cryotherapy (freezing) for cosmetic removal",
            "Curettage (scraping off) for cosmetic reasons",
            "Electrocautery (burning off)",
            "Shave excision if irritated by clothing"
        ),
        whenToSeeDoctor = listOf(
            "Growth looks unusual or different from others",
            "Growth changes rapidly",
            "Growth bleeds or becomes irritated",
            "Unsure if growth is seborrheic keratosis",
            "Want removal for cosmetic reasons"
        ),
        prognosis = "Completely harmless and will never become cancerous. Multiple growths are common and normal. Purely a cosmetic concern if bothersome.",
        faqs = listOf(
            FAQ(
                "Are seborrheic keratoses dangerous?",
                "No, they are completely harmless and never become cancerous. They're simply a cosmetic concern for some people."
            ),
            FAQ(
                "Why am I getting so many?",
                "Multiple seborrheic keratoses are very common and normal. Numbers typically increase with age, and having many is not concerning."
            ),
            FAQ(
                "Can I prevent them?",
                "There's no proven way to prevent seborrheic keratoses. They're a normal part of skin aging, like gray hair."
            )
        )
    )

    // Returns detailed information for Actinic Keratoses
    private fun getActinicKeratosesInfo() = DiseaseInformation(
        name = "Actinic Keratoses",
        shortName = "AK / Solar Keratosis",
        isMalignant = false,
        category = DiseaseCategory.PRECANCEROUS,
        shortDescription = "Precancerous rough patches caused by sun damage",
        overview = """
            Actinic keratoses (AKs), also called solar keratoses, are rough, scaly patches on the skin caused by years of sun exposure. They're considered precancerous because they can develop into squamous cell carcinoma if left untreated.
            
            AKs are extremely common, especially in people over 40 who have fair skin and a history of sun exposure. They typically appear on sun-exposed areas like the face, ears, scalp (in balding men), neck, forearms, and backs of hands.
            
            While most AKs don't become cancer, it's impossible to predict which ones will, so treatment is usually recommended.
        """.trimIndent(),
        symptoms = listOf(
            Symptom(
                "Rough, Scaly Patches",
                "Dry, rough patches that feel like sandpaper to touch"
            ),
            Symptom(
                "Varied Colors",
                "Pink, red, or light brown colored patches"
            ),
            Symptom(
                "Flat or Slightly Raised",
                "Usually flat but can be slightly raised or bumpy"
            ),
            Symptom(
                "Size Variation",
                "Typically small (less than 1 inch) but can vary"
            ),
            Symptom(
                "Tenderness or Itching",
                "May feel tender, itchy, or burn when touched"
            )
        ),
        causes = listOf(
            "Cumulative UV radiation exposure over years",
            "Tanning bed use",
            "Severe or frequent sunburns",
            "Living in sunny climates"
        ),
        riskFactors = listOf(
            "Fair skin, blonde or red hair, light eyes",
            "Age over 40",
            "History of significant sun exposure",
            "History of sunburns",
            "Outdoor occupation or lifestyle",
            "Weakened immune system",
            "Living near equator or at high altitude"
        ),
        prevention = listOf(
            "Daily broad-spectrum SPF 30+ sunscreen (most important!)",
            "Avoid sun during peak hours (10 AM - 4 PM)",
            "Wear protective clothing, wide-brimmed hats",
            "Never use tanning beds",
            "Check skin regularly for new spots",
            "Get professional skin exams annually if you have AKs"
        ),
        treatment = listOf(
            "Cryotherapy (freezing with liquid nitrogen) - most common",
            "Topical chemotherapy creams (fluorouracil, imiquimod)",
            "Photodynamic therapy (light-activated treatment)",
            "Chemical peels for multiple AKs",
            "Laser resurfacing",
            "Curettage (scraping off)"
        ),
        whenToSeeDoctor = listOf(
            "Rough spot doesn't heal or improve",
            "Spot grows rapidly",
            "Spot bleeds or becomes painful",
            "Spot becomes thick or horn-like",
            "You have multiple rough spots on sun-exposed areas"
        ),
        prognosis = "About 5-10% of untreated AKs develop into squamous cell carcinoma. With treatment, risk is greatly reduced. Ongoing sun protection and monitoring are essential.",
        faqs = listOf(
            FAQ(
                "Will my AK definitely become cancer?",
                "No. About 5-10% of untreated AKs develop into skin cancer over time, but most remain as AKs. Since we can't predict which will progress, treatment is recommended."
            ),
            FAQ(
                "Can AKs go away on their own?",
                "Occasionally, with strict sun protection, some early AKs may fade. However, most persist and new ones often develop without treatment and sun protection."
            ),
            FAQ(
                "If I have one AK, will I get more?",
                "Yes, if you have one AK, you'll likely develop more over time because they're caused by cumulative sun damage. Strict sun protection is crucial."
            )
        )
    )

    // Returns detailed information for Dermatofibramo
    private fun getDermatofibromaInfo() = DiseaseInformation(
        name = "Dermatofibroma",
        shortName = "Dermatofibroma",
        isMalignant = false,
        category = DiseaseCategory.BENIGN,
        shortDescription = "Harmless firm bumps that often appear after minor skin injury",
        overview = """
            A dermatofibroma is a common benign (harmless) skin nodule that feels firm to the touch. These growths are made of fibrous tissue and usually appear on the legs, though they can occur anywhere on the body.
            
            Dermatofibromas often develop at sites of minor trauma like insect bites or small cuts, though they can appear without any known injury. They're more common in women and typically affect adults.
            
            While harmless, dermatofibromas are permanent and rarely go away on their own. They cause no health problems and treatment is only needed if they're bothersome.
        """.trimIndent(),
        symptoms = listOf(
            Symptom(
                "Firm, Hard Bump",
                "Feels like a hard pea or button under the skin"
            ),
            Symptom(
                "Brown or Reddish Color",
                "Usually brown, reddish-brown, or tan colored"
            ),
            Symptom(
                "Dimple Sign",
                "Dimples inward when pinched from sides (characteristic sign)"
            ),
            Symptom(
                "Small Size",
                "Typically less than 1cm (about 1/2 inch) in diameter"
            ),
            Symptom(
                "Slow Growing",
                "Grows very slowly over months to years"
            )
        ),
        causes = listOf(
            "Often develops after minor skin injury",
            "Insect bites",
            "Splinters or thorn pricks",
            "Exact cause unknown in many cases"
        ),
        riskFactors = listOf(
            "Female gender (more common in women)",
            "Age 20-40 (most common age range)",
            "History of minor skin trauma",
            "Previous dermatofibromas (can have multiple)"
        ),
        prevention = listOf(
            "No known prevention methods",
            "These are benign reactions to minor trauma",
            "Normal part of skin's healing response"
        ),
        treatment = listOf(
            "No treatment necessary (completely harmless)",
            "Surgical excision if bothersome or for cosmetic reasons",
            "Cryotherapy (may flatten but not remove completely)",
            "Shave removal (may leave flat scar)",
            "Most people choose to leave them alone"
        ),
        whenToSeeDoctor = listOf(
            "Bump changes rapidly in size or color",
            "Bump becomes painful",
            "Bump bleeds without injury",
            "You want confirmation it's a dermatofibroma",
            "You want it removed for cosmetic reasons"
        ),
        prognosis = "Completely harmless and will never become cancerous. Usually permanent but cause no health problems. Purely a cosmetic concern if bothersome.",
        faqs = listOf(
            FAQ(
                "Is a dermatofibroma dangerous?",
                "No, dermatofibromas are completely harmless and never become cancerous. They're just firm nodules in the skin."
            ),
            FAQ(
                "Will it go away on its own?",
                "Dermatofibromas rarely disappear on their own. They're typically permanent, though they cause no harm."
            ),
            FAQ(
                "Should I have it removed?",
                "Removal is only necessary if the bump is bothersome, painful, or for cosmetic reasons. Most people leave them alone."
            )
        )
    )

    // Returns detailed information for Vascular Lesions
    private fun getVascularLesionsInfo() = DiseaseInformation(
        name = "Vascular Lesions",
        shortName = "Vascular Lesions",
        isMalignant = false,
        category = DiseaseCategory.BENIGN,
        shortDescription = "Benign abnormalities of blood vessels in the skin",
        overview = """
            Vascular lesions are abnormalities of blood vessels in the skin. They include common conditions like cherry angiomas (small red bumps), spider veins, and hemangiomas. Most are completely benign and harmless.
            
            These lesions appear red or purple because they contain blood vessels. They may blanch (turn white) temporarily when pressed. Most vascular lesions are either present at birth or develop with age.
            
            While usually harmless, some people choose to have visible vascular lesions treated for cosmetic reasons.
        """.trimIndent(),
        symptoms = listOf(
            Symptom(
                "Red or Purple Color",
                "Bright red, dark red, or purple appearance due to blood vessels"
            ),
            Symptom(
                "Blanches with Pressure",
                "May temporarily turn white when pressed, then refill with blood"
            ),
            Symptom(
                "Various Sizes",
                "Can range from pinpoint to several centimeters"
            ),
            Symptom(
                "Different Shapes",
                "Can be flat, raised, spider-like, or clustered"
            )
        ),
        causes = listOf(
            "Abnormal development of blood vessels",
            "Age-related changes (cherry angiomas)",
            "Genetic factors",
            "Hormonal changes",
            "Sun exposure (some types)",
            "Present at birth (some types)"
        ),
        riskFactors = listOf(
            "Age (cherry angiomas increase with age)",
            "Genetic predisposition",
            "Fair skin",
            "Pregnancy (can trigger new lesions)",
            "Family history of vascular lesions"
        ),
        prevention = listOf(
            "No proven prevention methods for most types",
            "Sun protection may prevent some types",
            "These are largely genetic or age-related"
        ),
        treatment = listOf(
            "No treatment necessary (most are harmless)",
            "Laser therapy for cosmetic removal",
            "Electrocautery (burning off small lesions)",
            "Sclerotherapy for spider veins",
            "Intense pulsed light (IPL) therapy",
            "Surgical excision for larger lesions"
        ),
        whenToSeeDoctor = listOf(
            "Lesion bleeds frequently",
            "Lesion grows rapidly",
            "Lesion becomes painful",
            "Unsure if lesion is vascular",
            "Want cosmetic removal"
        ),
        prognosis = "Most vascular lesions are harmless and require no treatment. They may increase in number with age but cause no health problems. Treatment is purely cosmetic.",
        faqs = listOf(
            FAQ(
                "Are cherry angiomas dangerous?",
                "No, cherry angiomas are completely harmless. They're just clusters of blood vessels and never become cancerous."
            ),
            FAQ(
                "Why am I getting more red spots?",
                "Cherry angiomas and some other vascular lesions increase with age. This is normal and not concerning."
            ),
            FAQ(
                "Can I remove them at home?",
                "No, never attempt to remove vascular lesions at home as they can bleed significantly. See a doctor for safe removal options."
            )
        )
    )
}
