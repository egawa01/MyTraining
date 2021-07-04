package com.rakudasoft.mytraining.data

import com.rakudasoft.mytraining.data.model.LoggedInUser

typealias OnSignInCompletedListener = (Result<LoggedInUser>) -> Unit
