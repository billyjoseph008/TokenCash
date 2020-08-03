package com.brainstormideas.tokencash.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Registro(
    var descripcion: String? = ""
)
