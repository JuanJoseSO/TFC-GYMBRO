package com.example.tfc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView

class ActivityEntrenamiento : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrenamiento)

        val wvReproductor=findViewById<WebView>(R.id.wvReproductor)
        val video="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/U5lV7oPW3CA?si=AfWeRu9obD7VmI10\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
        wvReproductor.loadData(video,"text/html","utf-8")

    }
}