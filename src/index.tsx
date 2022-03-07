import { NativeModules } from 'react-native';

type Location = {
  latitude: Number;
  longitude: Number;
}
type GeolocationType = {
  getCurrentLocation(): Promise<Location>;
  startLocationUpdates(): Promise<void>;
};

const { Geolocation } = NativeModules;

export default Geolocation as GeolocationType;