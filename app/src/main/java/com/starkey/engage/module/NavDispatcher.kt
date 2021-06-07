package com.starkey.engage.module

import android.content.Context
import androidx.navigation.NavController
import com.starkey.engage.view.model.EventDispatcher
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

public typealias NavigationCommand = NavController.(Context) -> Unit

@ActivityRetainedScoped
public class NavDispatcher @Inject constructor() : EventDispatcher<NavigationCommand>()