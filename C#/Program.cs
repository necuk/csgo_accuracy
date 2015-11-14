using System;
using System.IO;
using DemoInfo;
using System.Diagnostics;
using System.Collections.Generic;

namespace ParseData
{
    class MainClass
    {
        public static void Main(string[] args)
        {
            using (var input = File.OpenRead(args[0]))
            {
                var parser = new DemoParser(input);

                parser.ParseHeader();

                parser.WeaponFired += (sender, e) => {
                    Console.WriteLine("Weapon_fire");
                    Console.WriteLine("{");
                    Console.WriteLine("velx=" + e.Shooter.Velocity.X);
                    Console.WriteLine("vely=" + e.Shooter.Velocity.Y);
                    Console.WriteLine("weapon=" + e.Weapon.Weapon);
                    Console.WriteLine("playername=" + e.Shooter.Name);
                    Console.WriteLine("playersteamid=" + e.Shooter.SteamID);
                    Console.WriteLine("}");

                };

                parser.ParseToEnd();
                Console.WriteLine("End");
            }
        }
    }
}

