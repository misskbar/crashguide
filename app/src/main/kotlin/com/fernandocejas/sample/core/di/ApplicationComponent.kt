/**
 * Copyright (C) 2018 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fernandocejas.sample.core.di

import com.fernandocejas.sample.AndroidApplication
import com.fernandocejas.sample.core.di.viewmodel.ViewModelModule
import com.fernandocejas.sample.core.navigation.RouteActivity
import com.fernandocejas.sample.features.generateQR.GenerateQRFragment
import com.fernandocejas.sample.features.injured.InjuredFragment
import com.fernandocejas.sample.features.movies.MovieDetailsFragment
import com.fernandocejas.sample.features.movies.MoviesFragment
import com.fernandocejas.sample.features.onboarding.IsAnyInjuredFragment
import com.fernandocejas.sample.features.onboarding.OnBoardingFragment
import com.fernandocejas.sample.features.pointinmap.MapFragment
import com.fernandocejas.sample.features.signup.SignUpFragment
import com.fernandocejas.sample.features.thridpartyinformation.ScanQRActivity
import com.fernandocejas.sample.features.thridpartyinformation.ThirdPartyFragment
import com.fernandocejas.sample.features.thridpartyinformation.ThirdPartyListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ViewModelModule::class])
interface ApplicationComponent {
    fun inject(application: AndroidApplication)
    fun inject(routeActivity: RouteActivity)

    fun inject(moviesFragment: MoviesFragment)
    fun inject(movieDetailsFragment: MovieDetailsFragment)

    fun inject (onBoardingFragment: OnBoardingFragment)
    fun inject (isAnyInjuredFragment: IsAnyInjuredFragment)

    fun inject (injuredFragment: InjuredFragment)

    fun inject (mapsFragment: MapFragment)

    fun inject (thirdPartyFragment: ThirdPartyFragment)

    fun inject (signUpFragment: SignUpFragment)

    fun inject (generateQRFragment: GenerateQRFragment)

    fun inject (ThirdPartyListFragment: ThirdPartyListFragment)

    fun inject (scanQRActivity: ScanQRActivity)

}
