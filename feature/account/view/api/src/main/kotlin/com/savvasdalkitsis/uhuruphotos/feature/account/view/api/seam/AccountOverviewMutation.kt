/*
Copyright 2022 Savvas Dalkitsis

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.savvasdalkitsis.uhuruphotos.feature.account.view.api.seam

import com.savvasdalkitsis.uhuruphotos.feature.account.view.api.ui.state.AccountOverviewState
import com.savvasdalkitsis.uhuruphotos.feature.avatar.view.api.ui.state.AvatarState
import com.savvasdalkitsis.uhuruphotos.foundation.seam.api.Mutation

sealed class AccountOverviewMutation(
    mutation: Mutation<AccountOverviewState>
) : Mutation<AccountOverviewState> by mutation {

    data class AvatarUpdate(val avatarState: AvatarState) : AccountOverviewMutation({
        it.copy(avatarState = avatarState)
    })

    object ShowAccountOverview : AccountOverviewMutation({
        it.copy(showAccountOverview = true)
    })

    object HideAccountOverview : AccountOverviewMutation({
        it.copy(showAccountOverview = false)
    })

    object ShowLogOutConfirmation : AccountOverviewMutation({
        it.copy(showLogOutConfirmation = true)
    })

    object HideLogOutConfirmation : AccountOverviewMutation({
        it.copy(showLogOutConfirmation = false)
    })

}
