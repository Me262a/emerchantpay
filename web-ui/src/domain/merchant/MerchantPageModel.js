import MerchantViewModel from "./MerchantViewModel";

class MerchantPageView {
    constructor(content, totalPages, currentPage) {
        this.content = content.map(m =>
            new MerchantViewModel(m.id, m.name, m.description, m.email, m.status, m.totalTransactionSum)
        );
        this.totalPages = totalPages;
        this.currentPage = currentPage;
    }

    toPlainObject() {
        return {
            content: this.content.map(m => m.toPlainObject()),
            totalPages: this.totalPages,
            currentPage: this.currentPage
        };
    }
}
export default MerchantPageView;
