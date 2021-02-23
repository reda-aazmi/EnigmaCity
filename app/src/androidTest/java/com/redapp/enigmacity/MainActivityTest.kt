package com.redapp.enigmacity

import android.content.Intent
import android.provider.Settings
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.redapp.enigmacity.testUtils.RequirementsGranter.allowPermission
import com.redapp.enigmacity.testUtils.RequirementsGranter.assertPermissionGranted
import com.redapp.enigmacity.testUtils.RequirementsGranter.assertPermissionNotGranted
import com.redapp.enigmacity.testUtils.RequirementsGranter.denyPermission
import com.redapp.enigmacity.testUtils.RequirementsGranter.hasNeededPermission
import com.redapp.enigmacity.testUtils.RequirementsGranter.isLocationEnabled
import junit.framework.Assert.assertTrue
import org.hamcrest.core.AllOf.allOf
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get: Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    //The location permission must not be allowed in order to make this test
    @Test
    fun test01_displaySnackBar_whenLocationPermissionIsDenied(){
        if(!hasNeededPermission(LOCATION_PERMISSION)) {
            denyPermission(LOCATION_PERMISSION)
            assertPermissionNotGranted(LOCATION_PERMISSION)
            onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(R.string.request_grant_permission)))
            onView(allOf(withId(com.google.android.material.R.id.snackbar_action), withText(R.string.settings)))
                .perform(click())
            intended(hasAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS))
        }
    }

    //The location permission must not be allowed in order to make this test
    @Test
    fun test02_grantLocationPermission_whenLocationPermissionIsNotGranted(){
        if(!hasNeededPermission(LOCATION_PERMISSION)) {
            allowPermission(LOCATION_PERMISSION)
        }
        assertPermissionGranted(LOCATION_PERMISSION)
    }

    //The location feature must be disabled in order to make this test
    @Test
    fun test03_displaySnackBarAndLocationSettings_whenLocationIsDisabled(){
        if(!isLocationEnabled()){
            onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(R.string.turn_on_location_setting)))
            onView(allOf(withId(com.google.android.material.R.id.snackbar_action), withText(R.string.ok)))
                .perform(click())
            intended(hasAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

}


