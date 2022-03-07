import * as React from 'react';
import { StyleSheet, View, Text, PermissionsAndroid } from 'react-native';
import Geolocation from 'react-native-geolocation';

export default function App() {
  const [location] = React.useState<String | undefined>("No current location");

  React.useEffect(() => {
    PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION).then(result => {
      console.log('ACCESS_FINE_LOCATION result: ', result);
      Geolocation.startLocationUpdates().then(() => {
        console.log('Location updates started');
        // Geolocation.getCurrentLocation().then((location) => {
        //   console.log('Location: ', location)
        //   setTimeout(() => {
        //     Geolocation.getCurrentLocation().then((location) => {
        //       console.log('Location 2: ', location)
        //       Geolocation.getCurrentLocation().then((location) => {
        //         console.log('Location 3: ', location)
        //       });
        //     });
        //   }, 2000)
        // });
      }).catch((err) => {
        console.log('[BgLocation.init] err: ', err);
      }) 
    }).catch(err => {
      console.log('ACCESS_FINE_LOCATION err: ', err);
    })
  }, []);

  return (
    <View style={styles.container}>
      <Text>Location: {location}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
