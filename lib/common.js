const availableLanguages = ["fr", "en"];
const defaultLocale = "fr";

const getAvailableLanguages = () => {
    return availableLanguages;
};

const getDefaultLocale = () => {
    return defaultLocale;
};

module.exports = {
    getAvailableLanguages,
    getDefaultLocale
};


