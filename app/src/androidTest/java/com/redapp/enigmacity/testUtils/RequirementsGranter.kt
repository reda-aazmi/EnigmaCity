package com.redapp.enigmacity.testUtils

import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiObjectNotFoundException
import androidx.test.uiautomator.UiSelector
import org.junit.Assert
import java.lang.RuntimeException

private const val TAG = "PermissionGranter"
private const val DENY_BUTTON_INDEX = 0
private const val GRANT_BUTTON_INDEX = 1

object RequirementsGranter {

    private val instrumentationRegistry = InstrumentationRegistry.getInstrumentation()
    private val context = instrumentationRegistry.context

    fun allowPermission(permissionNeeded: String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && !hasNeededPermission(permissionNeeded))
        {
            allowPermissionWhenNeeded()
        }
    }

    private fun allowPermissionWhenNeeded() {
        try{
            val device: UiDevice = UiDevice.getInstance(instrumentationRegistry)
            val grantButtonOnPermissionDialog = UiSelector()
                .clickable(true)
                .checkable(false)
                .index(GRANT_BUTTON_INDEX)
            val allowPermissionsButton: UiObject = device.findObject(grantButtonOnPermissionDialog)
            if (allowPermissionsButton.exists()) {
                allowPermissionsButton.click()
            }
        }catch (e: UiObjectNotFoundException){
            Log.e(TAG, "allowPermissionIfNeeded: There is no permissions dialog to interact with", e)
        }
        catch (e: RuntimeException){
            Log.e(TAG, "allowPermissionIfNeeded: There is no permissions dialog to interact with", e)
            throw RuntimeException("There is no permissions dialog to interact with")
        }
    }




    fun denyPermission(permissionNeeded: String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && !hasNeededPermission(permissionNeeded))
        {
            denyPermissionWhenNeeded()
        }
    }

    private fun denyPermissionWhenNeeded() {
        try{
            val device: UiDevice = UiDevice.getInstance(instrumentationRegistry)
            val grantButtonOnPermissionDialog = UiSelector()
                .clickable(true)
                .checkable(false)
                .index(DENY_BUTTON_INDEX)
            val allowPermissionsButton: UiObject = device.findObject(grantButtonOnPermissionDialog)
            if (allowPermissionsButton.exists()) {
                allowPermissionsButton.click()
            }
        }catch (e: UiObjectNotFoundException){
            Log.e(TAG, "allowPermissionIfNeeded: There is no permissions dialog to interact with", e)
        }
        catch (e: RuntimeException){
            Log.e(TAG, "allowPermissionIfNeeded: There is no permissions dialog to interact with", e)
            throw RuntimeException("There is no permissions dialog to interact with")
        }
    }


    fun hasNeededPermission( permissionNeeded: String): Boolean{
        val permissionStatus: Int = context.checkSelfPermission(permissionNeeded)
        return permissionStatus == PackageManager.PERMISSION_GRANTED
    }

    fun assertPermissionGranted(permission: String) {
        val permissionValue = context.checkSelfPermission(permission)
        Assert.assertEquals(
            "Expected permission $permission was not granted;",
            PackageManager.PERMISSION_GRANTED,
            permissionValue
        )
    }

    fun assertPermissionNotGranted(permission: String) {
        val permissionValue = context.checkSelfPermission(permission)
        Assert.assertEquals(
            "Expected permission $permission was granted;",
            PackageManager.PERMISSION_DENIED,
            permissionValue
        )
    }

    fun isLocationEnabled():Boolean{
        var locationEnabled = false
        val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        try {
            locationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }catch (e: RuntimeException){
            Log.e(TAG, "checkDeviceLocationSettings: ",e )
        }
        return locationEnabled
    }
}