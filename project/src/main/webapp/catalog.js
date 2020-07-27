var photoURLS; 
var image;
var reviewArr;
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
                // console.log("this is not cool" + results.reviews);
                if (!photos) {
                    console.log("No Photos");
                    return;
                }

                photoURLS = [];
                reviewArr = [];
                for (var i = 0; i < photos.length; i++){
                    var url = photos[i].getUrl({maxWidth: 500, maxHeight: 500})
                    photoURLS.push(url);
                    if (i < 5) { // details request only returns up to 5 reviews
                        console.log(reviews[i]);
                        reviewArr.push(reviews[i]);
                    }
                }
                image = document.getElementById("image");
                image.src = photoURLS[0];
                var rating = document.getElementById("rating").innerText = results.rating + "/5";
                reviewArr.forEach(showReviews);
                // reviewSpace = document.getElementById("review-space").innerText =  reviewArr[0].text;
                console.log(reviewArr[0]);
            }
	    }
    );
}

var i = 0;

function showReviews(review) {
    var author = document.createElement("bold")
    author.innerHTML = review.author_name;
    var text = document.createElement("p");
    text.innerHTML = review.text
    reviewArea = document.getElementById("review-space");
    reviewArea.appendChild(author);
    reviewArea.appendChild(text);
    reviewArea.appendChild(document.createElement("hr"));
}

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

function showForm() {
    reviewForm = document.getElementById("review-form");
    reviewForm.removeAttribute("hidden");
}