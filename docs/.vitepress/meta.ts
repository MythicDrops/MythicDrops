// noinspection ES6PreferShortImport: IntelliJ IDE hint to avoid warning to use `~/contributors`, will fail on build if changed

/* Texts */
export const mythicDropsName = "MythicDrops";
export const mythicDropsShortName = "MythicDrops";
export const mythicDropsDescription = "Action RPG style drops in Spigot";

/* CDN fonts and styles */
export const googleapis = 'https://fonts.googleapis.com'
export const gstatic = 'https://fonts.gstatic.com'
export const font = `${googleapis}/css2?family=Readex+Pro:wght@200;400;600&display=swap`

/* GitHub and social links */
export const releases = "https://github.com/MythicDrops/MythicDrops/releases";
export const github = "https://github.com/MythicDrops/MythicDrops";
export const contributing = "https://github.com/MythicDrops/MythicDrops";
export const discord = "https://discord.gg/ANu94t2B9C";

/* Avatar/Image/Sponsors servers */
export const preconnectLinks = [googleapis, gstatic]
export const preconnectHomeLinks = [googleapis, gstatic]

/* PWA runtime caching urlPattern regular expressions */
export const pwaFontsRegex = new RegExp(`^${googleapis}/.*`, 'i')
export const pwaFontStylesRegex = new RegExp(`^${gstatic}/.*`, 'i')
