import contributors from "../contributors.json";

export interface Contributor {
  name: string;
  avatar: string;
}

export interface CoreTeam {
  avatar: string;
  name: string;
  github: string;
  twitter?: string;
  sponsors?: boolean;
  description: string;
}

const contributorsAvatars: Record<string, string> = {};

const getAvatarUrl = (name: string) =>
  import.meta.hot
    ? `https://github.com/${name}.png`
    : `/user-avatars/${name}.png`;

const contributorList = (contributors as string[]).reduce((acc, name) => {
  contributorsAvatars[name] = getAvatarUrl(name);
  acc.push({ name, avatar: contributorsAvatars[name] });
  return acc;
}, [] as Contributor[]);

const coreTeamMembers: CoreTeam[] = [
  {
    avatar: contributorsAvatars.ToppleTheNun,
    name: "Richard Harrah",
    github: "ToppleTheNun",
    twitter: "ToppleTheNun",
    sponsors: true,
    description:
      "Has been referred to as Gandalf<br>Working at SingleStone Consulting"
  }
];

export { coreTeamMembers, contributorList as contributors };
