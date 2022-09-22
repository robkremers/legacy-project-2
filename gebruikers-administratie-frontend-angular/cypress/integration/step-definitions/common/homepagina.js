const port = 4200
const baseurl = 'http://localhost:' + port + '/home'


class HomePagina {
    static visitHomePagina(gebruikersNaam){
        cy.login(gebruikersNaam)
        cy.visit(baseurl)
    }
}

export default HomePagina
