import contributorNames from "./contributor-names.json";

export interface Contributor {
  name: string;
  avatar: string;
  // required to download avatars from GitHub
  github: string;
  links?: SocialEntry[];
}

export interface SocialEntry {
  icon: string;
  link: string;
}

export interface CoreTeam {
  avatar: string;
  name: string;
  // required to download avatars from GitHub
  github: string;
  twitter: string;
  sponsor?: string;
  title?: string;
  org?: string;
  desc?: string;
  links?: SocialEntry[];
}

const contributorsAvatars: Record<string, string> = {};

const createLinksCoreTeam = (tm: CoreTeam): CoreTeam => {
  tm.links = [
    { icon: "github", link: `https://github.com/${tm.github}` },
    { icon: "twitter", link: `https://twitter.com/${tm.twitter}` },
  ];
  return tm;
};

const createLinksContributor = (tm: Contributor): Contributor => {
  tm.links = [{ icon: "github", link: `https://github.com/${tm.github}` }];
  return tm;
};

const getAvatarUrl = (name: string) =>
  import.meta.hot
    ? `https://github.com/${name}.png`
    : `/user-avatars/${name}.png`;

const plainContributors = (contributorNames as string[]).reduce((acc, name) => {
  contributorsAvatars[name] = getAvatarUrl(name);
  acc.push({ name, avatar: contributorsAvatars[name], github: name });
  return acc;
}, [] as Contributor[]);

export const contributors = plainContributors.map((tm) =>
  createLinksContributor(tm)
);

const plainTeamMembers: CoreTeam[] = [
  {
    avatar: getAvatarUrl("ToppleTheNun"),
    name: "Richard Harrah",
    github: "ToppleTheNun",
    twitter: "ToppleTheNun",
    sponsor: "https://github.com/sponsors/ToppleTheNun",
    title: "A full stack developer",
    desc: "DevEx evangelist, has been called Gandalf",
  },
];

const teamMembers = plainTeamMembers.map((tm) => createLinksCoreTeam(tm));

export { teamMembers };
