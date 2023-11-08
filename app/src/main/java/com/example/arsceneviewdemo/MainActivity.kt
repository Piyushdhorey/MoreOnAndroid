package com.example.arsceneviewdemo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isGone
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.math.Position

class MainActivity : AppCompatActivity() {

    lateinit var arSceneView: ArSceneView
    lateinit var arModelNode: ArModelNode
    lateinit var btnPlace: ExtendedFloatingActionButton


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arSceneView = findViewById(R.id.arSceneView)

        btnPlace = findViewById(R.id.btnPlaceHere)

        btnPlace.setOnClickListener{
            placeModel()
        }

        arModelNode = ArModelNode().apply {
            loadModelGlbAsync(
                glbFileLocation = "https://models.readyplayer.me/654227e0cb142fe4a4ee4c4f.glb",
                centerOrigin = Position(x = 0.0f, y = 0.0f, z = 0.0f)
            )

            {
                arSceneView.planeRenderer.isVisible = true
            }
            onAnchorChanged = {
                btnPlace.isGone
            }
        }

        arSceneView.addChild(arModelNode)
    }

    private fun placeModel() {
        arModelNode.anchor()
        arSceneView.planeRenderer.isVisible = false
    }
}