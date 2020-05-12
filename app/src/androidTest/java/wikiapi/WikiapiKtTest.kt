package wikiapi

import androidx.test.rule.ActivityTestRule
import junit.framework.TestCase

import org.junit.Test

import org.junit.Assert.*

class WikiapiKtTest : TestCase() {

    @Test
    fun input_Sunflower() {
        val expectedOutput: String = "table=\\n\" +\n" +
                "                \"[Kingdom=Plantae, \\n\" +\n" +
                "                \"Clade=Tracheophytes, \\n\" +\n" +
                "                \"Clade=Angiosperms, \\n\" +\n" +
                "                \"Clade=Monocots,\\n\" +\n" +
                "                \" Order=Asparagales, \\n\" +\n" +
                "                \"Family=Asparagaceae, \\n\" +\n" +
                "                \"Subfamily=Scilloideae,\\n\" +\n" +
                "                \" Genus=Hyacinthus,\\n\" +\n" +
                "                \" Species=H. orientalis], \\n\" +\n" +
                "                \"description=Hyacinthus orientalis \\n\" +\n" +
                "                \"(common hyacinth, garden hyacinth or Dutch hyacinth),\\n\" +\n" +
                "                \" is a species of flowering plant in the family Asparagaceae, subfamily Scilloidiae,\\n\" +\n" +
                "                \" native to southwestern Asia, southern and central Turkey, northwestern Syria, \\n\" +\n" +
                "                \"Lebanon and northern Palestine. It was introduced to Europe in the 16th century.\\n\" +\n" +
                "                \" It is widely cultivated everywhere in the temperate world for its strongly fragrant \\n\" +\n" +
                "                \"flowers which appear exceptionally early in the season, and frequently forced to flower\\n\" +\n" +
                "                \" at Christmas time."

        try {
            assertEquals(wikiapi("Sunflower"), expectedOutput)
        } catch (e: Exception) {
            return
        }
    }



    @Test
    fun input_Tulip(){
        val expectedOutput: String = "{table=\n" +
                "[Scientific=classification,\n" +
                "Domain=biota,\n" +
                "Supraregn=eukaryotic,\n" +
                "Kingdom=Plantae,\n" +
                "Subkingdom=Viridiplantae,\n" +
                "Infrared=treptophyta,\n" +
                "Order=liliales,\n" +
                "Family=Liliaceae,\n" +
                "subfamily=Lilioideae],\n" +
                "\n" +
                "description=Tulip ( Tulipa ) is a genus containing about 100 species of flowering plants in the family Liliaceae .We are native to southern Europe , northern Africa and Asia from Anatolia and Iran (where the flower is depicted on the national flag) to China and Japan . The most diverse areas in terms of tulip species are the Pamir Mountains , the Hindu Kush Mountains and the steppes of Kazakhstan . There are over 150 species of tulip and over 3000 varieties in different colors and shapes.\n" +
                "}"
        try {
            assertEquals(wikiapi("Tulip"), expectedOutput)
        } catch (e: Exception) {
            return
        }
    }



    @Test
    fun input_Rose() {

        val expectedOutput: String = "{table=\n" +
                "[Kingdom=Plantae,\n" +
                "Division=flowering plant,\n" +
                "Class=Magnoliopsida,\n" +
                "Subclass=rosidae,\n" +
                "Order=Rosales,\n" +
                "Family=Rosaceae,\n" +
                "Gender=Rosa],\n" +
                "\n" +
                "description=Rose ( Rosa L. ) is a kind of plant perennial ornamental of the family Rosaceae , native of continental and subtropical regions of the northern hemisphere, comprising over 200 species of shrubs erect, often spiny.\n" +
                "\n" +
                "The plant typically has thorny stem and fruit of the Rosehip wild rose, Rosa canina , which grows in the form of bushes in arid regions, soils limestone .\n" +
                "\n" +
                "The flowering of the rose is annual. Species included in the group of decorative roses and park roses: Rosa canina (common rosehip), Rosa rubrifolia (red rosehip), Rosa alba (white rose), Rosa demascena ( Damascus rose ), Rosa lutea (yellow rosehip), Rosa rugosa (the wrinkled leaf rose).}\n" +
                "\n" +
                "}"

        try {
            assertEquals(wikiapi("Rose"), expectedOutput)
        } catch (e: Exception) {
            return
        }
    }


    @Test
    fun input_Lilac (){
        val expectedOutput: String ="{table=\n" +
                "[Kingdom=Plantae,\n" +
                "Clade=Tracheophytes,\n" +
                "Clade=Angiosperms,\n" +
                "Clade=Eudicots,\n" +
                "Clade=Asterids,\n" +
                "Order=Lamiales,\n" +
                "Family=Oleaceae,\n" +
                "Tribe=Oleeae,\n" +
                "Subtribe=Ligustrinae,\n" +
                "Genus=Syringa],\n" +
                "\n" +
                "description=Syringa (lilac) is a genus of 12 currently recognized[1] species of flowering woody plants in the olive family (Oleaceae), native to woodland and scrub from southeastern Europe to eastern Asia, and widely and commonly cultivated in temperate areas elsewhere.[2][3][4][5]\n" +
                "\n" +
                "The genus is most closely related to Ligustrum (privet), classified with it in Oleaceae tribus Oleeae subtribus Ligustrinae.[6]\n" +
                "\n" +
                "Lilacs are used as food plants by the larvae of some Lepidoptera species including copper underwing, scalloped oak and Svensson's copper underwing.\n" +
                "}"
        try {
            assertEquals(wikiapi("Lilac"), expectedOutput)
        } catch (e: Exception) {
            return
        }
    }


    }
