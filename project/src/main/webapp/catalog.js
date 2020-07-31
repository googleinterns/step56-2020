var photoURLS; 
const image = document.getElementsByClassName("carousel-image");
const reviewArea = document.getElementById("review-space");
var reviewArr;

function showCatalog(placeID) {
    console.log ("this is the catalog")
    //returns photos associated with user search
    removeAllChildNodes(reviewArea);
    containerThree = document.getElementById("container-3").removeAttribute("hidden");
    messageContainer = document.getElementById("chat-div").removeAttribute("hidden");
    const request = {
        placeId: placeID,
        fields: ["photos", "rating", "reviews", "name", "formatted_address"]
    };

    service = new google.maps.places.PlacesService(map);
    service.getDetails(request, function callback(results, status) { // this request will return an array of photos as a result
        if (status == google.maps.places.PlacesServiceStatus.OK) {
            photos = results.photos;
            reviews = results.reviews;
            console.log(results);
            if (!photos) {
                console.log("No Photos");
                return;
            }

            console.log(image);
            photoURLS = [];
            reviewArr = [];
            for (var i = 0; i < photos.length; i++){
                var url = photos[i].getUrl({maxWidth: 300, maxHeight: 400})
                image[i].src = url;
                photoURLS.push(url);
                if (i < 5) { // details request only returns up to 5 reviews
                    console.log(reviews[i]);
                    reviewArr.push(reviews[i]);
                }
                
            }
            console.log(photoURLS);
            var rating = document.getElementById("rating").innerText = results.name + ": "  + results.rating + "/5";
            var address = document.getElementById("address").innerText = results.formatted_address;
            reviewArr.forEach(showReviews);
            console.log(reviewArr[0]);
        }
    }
    );
}

var i = 0;

function showReviews(review) {
    var author = document.createElement("bold");
    author.setAttribute("class", "container font-weight-bold px-0");
    author.innerHTML = review.author_name;
    var text = document.createElement("p");
    text.setAttribute("class", "container")
    text.innerHTML = "\t" + review.text + "\t" +review.rating + "/5";
    reviewArea.appendChild(author);
    reviewArea.appendChild(text);
    reviewArea.appendChild(document.createElement("hr"));
}

function removeAllChildNodes(parent) {
    while (parent.firstChild) {
        parent.removeChild(parent.firstChild);
    }
}

function showForm() {
    reviewForm = document.getElementById("review-form");
    reviewForm.removeAttribute("hidden");
}