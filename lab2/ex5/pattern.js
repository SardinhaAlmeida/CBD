pattern = function(display) {

        var number = ""
        var rev_number = ""
        var full = false;
        for(var i = 0; i < display.length; i++) {
            if (full === true) {
                number += display[i];
            }
            if(display[i] === "-") {
                full = true;
            }
        }
        
        for (var i = number.length - 1; i >= 0; i--) {
            rev_number += number[i];
        }

        return (number === rev_number) 
}

db.phones.find({$expr: { $function: {
    body: pattern,
    args: ["$display"],
    lang: "js"
} }})
    