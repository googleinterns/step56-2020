var photoURLS; 
var image;

function execute() {
    initMap();
    search();
    getCatalog();
}

function showCatalog(placeID) {
    //returns photos associated with user search
        const request = {
            placeId: placeID,
            fields: ["photos"]
        };

        service = new google.maps.places.PlacesService(map);
        service.getDetails(request, function callback(results, status) { // this request will return an array of photos as a result
            if (status == google.maps.places.PlacesServiceStatus.OK) {
                photos = results.photos;
                if (!photos) {
                    console.log("No Photos");
                    return;
                }

                photoURLS = [];
                console.log(photoURLS);
                for (var i = 0; i < photos.length; i++){
                    var url = photos[i].getUrl({maxWidth: 500, maxHeight: 500})
                    photoURLS.push(url);
                    console.log("added new url: ", url);
                }
                image = document.getElementById("image");
                image.src = photoURLS[0];
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
