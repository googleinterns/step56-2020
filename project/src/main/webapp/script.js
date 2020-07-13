var map;
var autocomplete;
var photoURLS; 
var image;

function execute() {
    initMap();
    search();
    getCatalog();
}

function initMap() {
    map = new google.maps.Map(document.getElementById("map"), {
        center: { lat: -33.866, lng: 151.196 },
        zoom: 15
    });
}

function search() {
    // autocompleted suggestions of user searches
    var input = document.getElementById("pac-input");
    autocomplete = new google.maps.places.Autocomplete(input);
    autocomplete.setFields(["place_id", "geometry"]);
}

function getCatalog() {
    //returns photos associated with user search
    autocomplete.addListener("place_changed", function() {
        var place = autocomplete.getPlace();

        if (!place.geometry) {
            return;
        }

        var search = document.getElementById("place-id").innerText = place.place_id;

        var request = {
            placeId: place.place_id,
            fields: ["photos"]
        };

        service = new google.maps.places.PlacesService(map);
        service.getDetails(request, function callback(results, status) { // this request will return an array of photos as a result
            if (status == google.maps.places.PlacesServiceStatus.OK) {
                photos = results.photos;
                if (!photos) {
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
                image.setAttribute("src", photoURLS[0]);
            }
        });
    });
}

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

// function resetPhotoURLs() {
//     photoURLS.length = 0;
//     console.log("this function works")
// }