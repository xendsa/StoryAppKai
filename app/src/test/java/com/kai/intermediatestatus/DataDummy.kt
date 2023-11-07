package com.kai.intermediatestatus

import com.kai.intermediatestatus.data.response.ListStoryItem

object DataDummy {
//"id":"story-Z15tywBkGPxkmed6","name":"kai","description":"ini test","photoUrl":"https://story-api.dicoding.dev/images/stories/photos-1699368362948_xaFSQQvH.jpg"
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100){
            val story = ListStoryItem(
                id = "story-Z15tywBkGPxkmed6",
                name = "kai",
                description = "ini test",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1699368362948_xaFSQQvH.jpg",
                createdAt = "2023-11-07-02T13:49:37.774z",
                lat = 35.6764,
                lon = 139.6500
            )
            items.add(story)
        }
        return items
    }
}