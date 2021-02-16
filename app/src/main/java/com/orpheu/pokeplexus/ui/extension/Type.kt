package com.orpheu.pokeplexus.ui.extension

import com.orpheu.pokeplexus.R
import com.orpheu.pokeplexus.domain.model.Type

fun Type.getImageResource() = when (this) {
    Type.water -> R.drawable.ic_water
    Type.steel -> R.drawable.ic_steel
    Type.rock -> R.drawable.ic_rock
    Type.psychic -> R.drawable.ic_psychic
    Type.poison -> R.drawable.ic_poison
    Type.normal -> R.drawable.ic_normal
    Type.ice -> R.drawable.ic_ice
    Type.ground -> R.drawable.ic_ground
    Type.grass -> R.drawable.ic_grass
    Type.ghost -> R.drawable.ic_ghost
    Type.flying -> R.drawable.ic_flying
    Type.fire -> R.drawable.ic_fire
    Type.fighting -> R.drawable.ic_fighting
    Type.fairy -> R.drawable.ic_fairy
    Type.electric -> R.drawable.ic_electric
    Type.dragon -> R.drawable.ic_dragon
    Type.dark -> R.drawable.ic_dark
    Type.bug -> R.drawable.ic_bug
}

fun Type.getColorResource() = when (this) {
    Type.water -> R.color.water
    Type.steel -> R.color.steel
    Type.rock -> R.color.rock
    Type.psychic -> R.color.psychic
    Type.poison -> R.color.poison
    Type.normal -> R.color.normal
    Type.ice -> R.color.ice
    Type.ground -> R.color.ground
    Type.grass -> R.color.grass
    Type.ghost -> R.color.ghost
    Type.flying -> R.color.flying
    Type.fire -> R.color.fire
    Type.fighting -> R.color.fighting
    Type.fairy -> R.color.fairy
    Type.electric -> R.color.electric
    Type.dragon -> R.color.dragon
    Type.dark -> R.color.dark
    Type.bug -> R.color.bug
}

fun Type.getNameStringResource() = when (this) {
    Type.water -> R.string.pokemon_type_water
    Type.steel -> R.string.pokemon_type_steel
    Type.rock -> R.string.pokemon_type_rock
    Type.psychic -> R.string.pokemon_type_psychic
    Type.poison -> R.string.pokemon_type_poison
    Type.normal -> R.string.pokemon_type_normal
    Type.ice -> R.string.pokemon_type_ice
    Type.ground -> R.string.pokemon_type_ground
    Type.grass -> R.string.pokemon_type_grass
    Type.ghost -> R.string.pokemon_type_ghost
    Type.flying -> R.string.pokemon_type_flying
    Type.fire -> R.string.pokemon_type_fire
    Type.fighting -> R.string.pokemon_type_fighting
    Type.fairy -> R.string.pokemon_type_fairy
    Type.electric -> R.string.pokemon_type_electric
    Type.dragon -> R.string.pokemon_type_dragon
    Type.dark -> R.string.pokemon_type_dark
    Type.bug -> R.string.pokemon_type_bug
}
