const { watch } = require("gulp");
const browserSync = require("browser-sync").create()

function serve() {

    watch(["*.html", "assets/*"], (done) => {
        browserSync.reload()
        done()
    })

    browserSync.init({
        server: {
            baseDir: "./"
        },
        logLevel: "info",
        open: false
    })
}

exports.serve = serve
exports.default = serve