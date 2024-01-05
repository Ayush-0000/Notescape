package com.example.loginsignup

import android.media.Image

data class NoteItem(val noteId: String,val title: String, val description: String ){
    constructor():this("","","")
}
