package wikiapi

import androidx.test.rule.ActivityTestRule
import junit.framework.TestCase
import org.hamcrest.Matchers.array

import org.junit.Test

import org.junit.Assert.*
import java.util.*

class WikiapiKtTest : TestCase() {

    val actualOutput: ActivityTestRule< wikiapi > = ActivityTestRule(
        wikiapi::class.java)


    val excpectedOutput: String = "table=\n" +
            "[Kingdom=Plantae, \n" +
            "Clade=Tracheophytes, \n" +
            "Clade=Angiosperms, \n" +
            "Clade=Monocots,\n" +
            " Order=Asparagales, \n" +
            "Family=Asparagaceae, \n" +
            "Subfamily=Scilloideae,\n" +
            " Genus=Hyacinthus,\n" +
            " Species=H. orientalis], \n" +
            "description=Hyacinthus orientalis \n" +
            "(common hyacinth, garden hyacinth or Dutch hyacinth),\n" +
            " is a species of flowering plant in the family Asparagaceae, subfamily Scilloidiae,\n" +
            " native to southwestern Asia, southern and central Turkey, northwestern Syria, \n" +
            "Lebanon and northern Palestine. It was introduced to Europe in the 16th century.\n" +
            " It is widely cultivated everywhere in the temperate world for its strongly fragrant \n" +
            "flowers which appear exceptionally early in the season, and frequently forced to flower\n" +
            " at Christmas time."
    @Test
    fun inputTest() {
        try {
            wikiapi("Floarea-soarelui")
            wikiapi("Trandafir")
            wikiapi("Lalea")
            wikiapi("Mac")

            throw IllegalArgumentException()
        } catch (e: IllegalArgumentException) {
            return
        }
    }

    @Test
    fun outputTest(){
    try{
        wikiapi("Floarea-soarelui")
        assertEquals(wikiapi("Floarea-soarelui"), excpectedOutput)
        throw IllegalArgumentException()
    }
    catch(e: IllegalArgumentException)
    {
        return
    }}




    @Test
    fun outputNull(){
   /* try{
        wikiapi("Floarea-soarelui")
        assertNotNull(wikiapi("Floarea-soarelui"))
            throw NullPointerException()

    }
    catch(e: NullPointerException)
    {
        return
    }*/

    }



}