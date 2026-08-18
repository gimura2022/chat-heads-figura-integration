{
  description = "logs reader mod dev shell flake";

  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { nixpkgs, flake-utils, ... }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = (import nixpkgs) {
          inherit system;
        };

        buildInputs = with pkgs; [
          glfw3-minecraft
          openal

          alsa-lib
          libjack2
          libpulseaudio
          pipewire

          libGL
          libx11
          libxcursor
          libxext
          libxrandr
          libxxf86vm

          udev

          vulkan-loader
          flite
        ];
        nativeBuildInputs = with pkgs; [
          openjdk21
          gradle_9
        ];
      in {
        devShells.default = pkgs.mkShell {
          inherit buildInputs nativeBuildInputs;
          LD_LIBRARY_PATH = pkgs.lib.makeLibraryPath buildInputs;
        };
      }
    );
}
