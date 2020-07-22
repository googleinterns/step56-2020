var photoURLS; 
var image;
var reviewsArr;
var reviewSpace;

function showCatalog(placeID) {
    console.log ("this is the catalog")
    //returns photos associated with user search
        const request = {
            placeId: placeID,
            fields: ["photos", "rating", "reviews"]
        };

        service = new google.maps.places.PlacesService(map);
        service.getDetails(request, function callback(results, status) { // this request will return an array of photos as a result
            if (status == google.maps.places.PlacesServiceStatus.OK) {
                photos = results.photos;
                reviews = results.reviews;
                if (!photos) {
                    console.log("No Photos");
                    return;
                }

                photoURLS = [];
                reviewsArr = [];
                console.log(photoURLS);
                for (var i = 0; i < photos.length; i++){
                    var url = photos[i].getUrl({maxWidth: 500, maxHeight: 500})
                    photoURLS.push(url);
                    if (i < 5) { // details request only returns up to 5 reviewa
                        reviewsArr.push(reviews[i]);
                    }
                    console.log("added new url: ", url);
                }
                image = document.getElementById("image");
                image.src = photoURLS[0];
                var rating = document.getElementById("rating").innerText = results.rating + "/5";
                reviewSpace = document.getElementById("reviews").innerText =  reviewArr[0];
            } else {
	    }
        });
    };

var i = 0;

function next() {
    i += 1;
    if (i < photoURLS.length) {
        image = document.getElementById("image");
        image.setAttribute("src", photoURLS[i]);
    }
}

function previous() {
    i -= 1;
    if (i > 0) {
        image = document.getElementById("image");
        image.setAttribute("src", photoURLS[i]);
    }
}